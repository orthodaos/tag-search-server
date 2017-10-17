package ru.slrscrt.http.model


/**
  * Информация о тэгах возвращаемая из API Stack Overflow
  */
case class TagItems(
                     items: Seq[TagInfo]
                   )

/**
  * Информация о тэге возвращаемая из API Stack Overflow
  */
case class TagInfo(
                    tags: Set[String],
                    is_answered: Boolean
                  )





