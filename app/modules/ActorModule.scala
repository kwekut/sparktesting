package modules

import com.google.inject.{ AbstractModule, Provides }
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.Play
import play.api.Play.current
import com.google.inject.name.Names
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import com.google.inject.name.Named
import play.Logger
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import play.api.libs.openid.OpenIdClient
import play.api.libs.ws.WSClient
import play.api.{ OptionalSourceMapper, Configuration }
import com.typesafe.config.ConfigFactory
import play.api.Play.current
import actors.SparkActor

/**
 * The Guice module which wires all Silhouette dependencies.
 */
class ActorModule extends AbstractModule with AkkaGuiceSupport  {

  /**
   * Configures the module.
   */
  def configure() {
    Logger.debug("Binding actor implementations.")
    bindActor[SparkActor]("spark-actor")

  }

}