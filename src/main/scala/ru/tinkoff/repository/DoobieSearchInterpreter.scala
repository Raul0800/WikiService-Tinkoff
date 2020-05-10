package ru.tinkoff.repository

import cats.effect.IO
import doobie.Transactor
import ru.tinkoff.domain.{Article, SearchArticle}
import java.time.LocalDateTime
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.util.ExecutionContexts
import doobie.util.{Read, Write}
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.implicits.javatime._

object SearchSQL {

  implicit val searchArticleRead: Read[SearchArticle] =
    Read[(Long, Long, String, Array[String])].map { case (a, b, c, d) => SearchArticle(a, b, c, d) }

  def find(title: String): doobie.Query0[SearchArticle] =
    sql"""SELECT
          cast(extract(epoch from create_timestamp) as BIGINT) as create_timestamp,
          cast(extract(epoch from "timestamp") as BIGINT) as "timestamp",
          title,
          auxiliary_text
        FROM public."Articles" where title like $title;"""
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
