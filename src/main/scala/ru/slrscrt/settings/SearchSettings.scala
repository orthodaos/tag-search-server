package ru.slrscrt.settings

import akka.actor.ExtendedActorSystem
import akka.http.scaladsl.{Http, HttpsConnectionContext}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.sslconfig.akka.AkkaSSLConfig
import com.typesafe.sslconfig.ssl.{SSLConfigParser, SSLConfigSettings}
import com.typesafe.sslconfig.util.EnrichedConfig

/**
  * @author Roman Maksyutov
  */
private[settings] class SearchSettings(config: Config)(implicit extendedSystem: ExtendedActorSystem) extends PartialSettings(config) {


  object Connection extends PartialSettings("connection") {

    val protocol: String = "protocol".str
    val host: String = "host".str
    val port: Int = "port"

    def httpsContext: HttpsConnectionContext = Http().createClientHttpsContext(SSLConfig.akkSSLConfig)

    private object SSLConfig extends PartialSettings("ssl-config") {

      val akkSSLConfig = new AkkaSSLConfig(extendedSystem, parseSslConfig("ssl"))

      def parseSslConfig(sslConfig: Config): SSLConfigSettings = {
        val cfg: Config = sslConfig.withFallback(ConfigFactory.defaultReference().getConfig("ssl-config"))
        val parser = new SSLConfigParser(EnrichedConfig(cfg), getClass.getClassLoader)
        parser.parse()
      }
    }

  }

  val url: String = "url".str

  val pageSize: Int = "page-size"
  val order: String = "order".str
  val sort: String = "sort".str
  val site: String = "site".str

  val parallelism: Int = "parallelism"

}
