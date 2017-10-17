import Groups._
import Versions._
import sbt._

/**
  * @author Roman Maksyutov
  */
object Dependencies {

  private val TestConfiguration = "test"

  val LibraryDependencies: Seq[ModuleID] = Seq(

    `com.typesafe.akka` %% "akka-actor" % Akka,
    `com.typesafe.akka` %% "akka-stream" % Akka,
    `com.typesafe.akka` %% "akka-agent" % Akka,
    `com.typesafe.akka` %% "akka-slf4j" % Akka,
    `com.typesafe.akka` %% "akka-testkit" % Akka % TestConfiguration,
    `com.typesafe.akka` %% "akka-stream-testkit" % Akka % TestConfiguration,


    `com.typesafe.akka` %% "akka-http" % AkkaHttp,
    `com.typesafe.akka` %% "akka-http-xml" % AkkaHttp,
    `com.typesafe.akka` %% "akka-http-core" % AkkaHttp,
    `com.typesafe.akka` %% "akka-http-jackson" % AkkaHttp,
    `com.typesafe.akka` %% "akka-http-testkit" % AkkaHttp % TestConfiguration,

    `org.scalatest` %% "scalatest" % ScalaTest % TestConfiguration,

    `org.slf4j` % "slf4j-api" % Slf4j,

    `ch.qos.logback` % "logback-classic" % Logback,
    `ch.qos.logback` % "logback-core" % Logback,

    `org.json4s` %% "json4s-core" % Json4s,
    `org.json4s` %% "json4s-jackson" % Json4s,

  )

}



