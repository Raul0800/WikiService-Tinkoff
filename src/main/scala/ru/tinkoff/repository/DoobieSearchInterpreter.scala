package ru.tinkoff.repository

import java.nio.file.Files

import cats.effect.IO
import doobie.Transactor
import ru.tinkoff.domain.{Article, SearchArticle}
import doobie.implicits._
import java.nio.file.{Files, Path}
import java.time.LocalDateTime
import doobie.util.{Read, Write}
import doobie.util.update.Update
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.util.ExecutionContexts
import doobie.util.{Read, Write}
import cats.implicits._
//import com.example.domain.Catalog
import doobie._
import doobie.implicits._
import doobie.implicits.javatime._

private object SearchSQL {

  implicit val articleWrite: Write[Article] =
    Write[(LocalDateTime, LocalDateTime, String, String, Array[String], String, Array[String])]
      .contramap[Article](
        x =>
          (
            x.create_timestamp.toLocalDateTime,
            x.timestamp.toLocalDateTime,
            x.language,
            x.wiki,
            x.category,
            x.title,
            x.auxiliary_text
          )
      )

  implicit val searchArticleRead: Read[SearchArticle] =
    Read[(Long, Long, String, Array[String])].map { case (a, b, c, d) => SearchArticle(a, b, c, d) }

  def find(title: String) =
    sql"""SELECT
          cast(extract(epoch from create_timestamp) as BIGINT) as create_timestamp,
          cast(extract(epoch from "timestamp") as BIGINT) as "timestamp",
          title,
          auxiliary_text
        FROM public."Articles" where title = $title;"""
      .query[SearchArticle]
}

class DoobieSearchInterpreter(val xa: Transactor[IO]) {

  def findArticleByTitle(title: String): IO[List[SearchArticle]] =
    SearchSQL
      .find(title)
      .to[List]
      .transact[IO](xa)
}

object DoobieSearchInterpreter {
  def apply(xa: Transactor[IO]): DoobieSearchInterpreter = new DoobieSearchInterpreter(xa)
}
