package ru.slrscrt.http

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.settings.ServerSettings
import ru.slrscrt.http.route.Routing
import ru.slrscrt.service.ExecutionContextSupport
import ru.slrscrt.settings.SettingsSupport
import ru.slrscrt.util.Logging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Сервер
  *
  * @author Roman Maksyutov
  */
class HttpServer(implicit val system: ActorSystem, startAt: Long)
  extends SettingsSupport
    with ExecutionContextSupport
    with Logging {

  import settings.materializer

  def startServer() {

    import settings.http._

    val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(
      handler = new Routing(),
      interface = host,
      port = port,
      settings = ServerSettings(settings.config)
    )

    val startupDuration = Duration(System.currentTimeMillis - startAt, TimeUnit.MILLISECONDS)

    bindingFuture onComplete {
      case Success(serverBinding) =>

        //shutdown Hook
        scala.sys.addShutdownHook {
          shutdownServer(serverBinding.unbind)
        }

        import ru.slrscrt.util.DateTimeUtils._

        logger.boldTable(
          s"""
             |HOST: $host
             |PORT: :$port
             |BINDING AT: http:/${serverBinding.localAddress}
             |STARTUP TIME: ${startupDuration.description}
             |""".stripMargin,
          "SEARCH-TAG SERVER")


      case Failure(e) =>
        logger.error("Error on startup server:", e)
        logger.boldTable(
          s"""
             |HOST: $host
             |PORT: :$port
             |ERROR: $e
             |""".stripMargin,
          "SEARCH-TAG SERVER")
        shutdownServer()
    }

  }


  private def shutdownServer(f: => Future[Unit] = Future.successful(())): Unit = {
    import scala.concurrent.duration._

    logger.table("SEARCH-TAG server terminating ...")
    Await.result(f transformWith {
      case Success(_) =>
        shutdownAll()

      case Failure(e) =>
        logger.error("Error during shutdown server:", e)
        shutdownAll()

    }, 60 seconds)
    logger.boldTable("SEARCH-TAG server terminated")
  }


  private def shutdownAll(): Future[Terminated] = {
    logger.table("Shutdown all ShutdownSupport services")

    logger.table("Shutdown all http connection pools")
    Http().shutdownAllConnectionPools()

    logger.table("Shutdown materializer")
    materializer.shutdown()

    logger.table("Terminate actor system")
    system.terminate()
  }


}