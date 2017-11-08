name := """testspark"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.stripe" % "stripe-java" % "1.33.0",
  "com.mohiva" %% "play-silhouette" % "3.0.4",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "org.apache.kafka" % "kafka-clients" % "0.9.0.0",
  "org.apache.spark" % "spark-core_2.11" % "1.6.0",
  "com.datastax.spark" % "spark-cassandra-connector_2.11" % "1.6.0-M2",
  "org.apache.hadoop"  % "hadoop-client" % "2.6.0",
  "com.sun.mail" % "javax.mail" % "1.5.5"
)

dependencyOverrides ++= Set(
  "com.google.guava" % "guava" % "16.0.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
