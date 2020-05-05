package ru.tinkoff.domain

import java.time.ZonedDateTime

final case class Article(
    create_timestamp: ZonedDateTime,
    timestamp: ZonedDateTime,
    language: String,
    wiki: String,
    category: Array[String],
    title: String,
    auxiliary_text: Array[String]
)

final case class SearchArticle(create_timestamp: Long, timestamp: Long, title: String, auxiliary_text: Array[String])

final case class Catalog(category: Array[String], amount: Int)
