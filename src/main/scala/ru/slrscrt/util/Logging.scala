package ru.slrscrt.util

import org.slf4j.{Logger, LoggerFactory}
import ru.slrscrt.util.AdvancedTableBuilder.{BoldBorder, DoubleBorder, LightBorder}

/**
  * @author Roman Maksyutov
  */
trait Logging {
  self =>

  val logger: Logger = LoggerFactory getLogger getClass

  implicit class LoggerExt(logger: org.slf4j.Logger) {

    def info(obj: Any): Unit = logger.info("{}", obj)

    def debug(obj: Any): Unit = logger.debug("{}", obj)

    def warn(obj: Any): Unit = logger.warn("{}", obj)

    def error(obj: Any): Unit = logger.error("{}", obj)

    def table[T](obj: T): Unit = table(obj, None)

    def table[T](obj: T, title: String): Unit = table(obj, Some(title))

    def table[T](obj: T, title: String, alignment: TableAlignment): Unit = table(obj, Some(title), alignment)

    def table[T](obj: T, title: Option[String], alignment: TableAlignment = TableAlignments.Left): Unit =
      logger.info("{}",
        new AdvancedTableBuilder(
          title,
          obj,
          alignment, LightBorder
        )
      )


    def boldTable[T](obj: T): Unit = boldTable(obj, None)

    def boldTable[T](obj: T, title: String): Unit = boldTable(obj, Some(title))

    def boldTable[T](obj: T, title: String, alignment: TableAlignment): Unit = boldTable(obj, Some(title), alignment)

    def boldTable[T](obj: T, title: Option[String], alignment: TableAlignment = TableAlignments.Left): Unit =
      logger.info("{}",
        new AdvancedTableBuilder(
          title,
          obj,
          alignment, BoldBorder
        )
      )

    def doubleTable[T](obj: T): Unit = doubleTable(obj, None)

    def doubleTable[T](obj: T, title: String): Unit = doubleTable(obj, Some(title))

    def doubleTable[T](obj: T, title: String, alignment: TableAlignment): Unit = doubleTable(obj, Some(title), alignment)

    def doubleTable[T](obj: T, title: Option[String], alignment: TableAlignment = TableAlignments.Left): Unit =
      logger.info("{}",
        new AdvancedTableBuilder(
          title,
          obj,
          alignment, DoubleBorder
        )
      )

  }

}
