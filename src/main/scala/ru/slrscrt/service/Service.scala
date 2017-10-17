package ru.slrscrt.service

import akka.Done
import akka.http.scaladsl.util.FastFuture
import ru.slrscrt.settings.SettingsSupport
import ru.slrscrt.util.Logging

import scala.concurrent.Future
import scala.util.{Success, Try}

/**
  * @author Roman Maksyutov
  */
trait Service extends SettingsSupport with ExecutionContextSupport with Logging {


  val serviceName: String = this.getClass.getSimpleName

  val EmptyFuture: Future[Unit] = FastFuture.successful(Unit)

  val NoneFuture: Future[None.type] = FastFuture.successful(None)

  val DoneFuture: Future[Done] = FastFuture.successful(Done)

  val SuccessDone: Try[Done] = Success(Done)

  implicit def any2Future[T](t: T): Future[T] = FastFuture.successful(t)

  logger.boldTable("Starting service", Some(serviceName))
}