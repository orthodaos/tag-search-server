package ru.slrscrt.util

/**
  * @author Roman Maksyutov
  */
sealed class TableAlignment

object TableAlignments {
  val Left = new TableAlignment
  val Center = new TableAlignment
  val Right = new TableAlignment
}