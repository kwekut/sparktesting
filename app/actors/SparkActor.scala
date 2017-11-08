package actors

import akka.actor.{ActorRef, ActorSystem, Props, Actor, PoisonPill}
import akka.routing.FromConfig
import akka.pattern.pipe
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
// import com.datastax.spark.connector._
// import com.datastax.spark.connector.cql.CassandraConnector
import javax.inject._
import com.google.inject.name.Named
import com.google.inject.assistedinject.Assisted
import java.util.UUID
import play.Logger
import org.joda.time.LocalTime
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.LocalDateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import com.google.common.io.Files
import java.io.File
import scala.util.Try
import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import akka.routing._
import com.typesafe.config.ConfigFactory
//import language.postfixOps
import java.util.concurrent.TimeUnit
//import scala.collection.JavaConverters._
import scala.concurrent.duration._
 
object SparkActor{
  case object Call
}


object Spark {
  val sparkConf = {
    val conf = new SparkConf(true)
      //.setMaster("mesos://zk://192.168.33.11:2181/mesos")
      .setMaster("local")
      .setAppName("TestSpark")
      //.set("spark.executor.uri", "http://d3kbcqa49mib13.cloudfront.net/spark-1.6.0-bin-hadoop2.6.tgz")
      //.set("spark.executor.uri", "https://www.dropbox.com/s/8szh6z3flop0cxc/spark-1.6.0-bin-hadoop2.6.tgz?dl=1#")
      //.set("spark.executor.uri", "https://spark-kwekut.s3.amazonaws.com/spark-1.6.0-bin-hadoop2.6.tgz")
      .set("spark.executor.uri", "file:/C:/Users/kwekut/.ivy2/cache/org.apache.spark/spark-core_2.11/jars/spark-core_2.11-1.6.0.jar")
      //.set("spark.mesos.executor.home", "/etc/spark-1.6.0-bin-hadoop2.6")
      .set("spark.executor.memory", "512m")
      .set("spark.driver.memory", "512m")
      .set("spark.cores.max", "1")
      .set("spark.executor.cores", "1")
      .set("spark.driver.cores", "1")
      conf
    }
  val sc = new SparkContext(sparkConf)
}

class SparkActor extends Actor {
  import SparkActor._
  import Spark._
  val system = akka.actor.ActorSystem("system")
  import system.dispatcher
  val dtz: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-mm-dd HH:MM:SS Z")

  def receive = {

    case  Call =>
        val date = dtz.parseDateTime(DateTime.now.toString("yyyy-mm-dd HH:MM:SS Z")).toString
        val input = sc.textFile("kjvdat.txt").map(line => line.toLowerCase)
        input.cache
        val wc = input
          .flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))
          .map(word => (word, 1))
          .reduceByKey((count1, count2) => count1 + count2)
        val out = "kjv-wc2"
        wc.saveAsTextFile(out)

        val logData = sc.parallelize(1 to 100).count()
        Logger.info("Count Time : Answer:" + date.toString + "=>  " + out)


  }

      system.scheduler.schedule(10 seconds, 120 seconds) {
        val tm = dtz.parseDateTime(DateTime.now.toString("yyyy-mm-dd HH:MM:SS Z")).toString
        self ! Call
        Logger.info("You have sent a spark Shop Call:" + tm.toString)
      }

}

