package ru.tinkoff.repository

import java.time.LocalDateTime

import cats.effect.IO
import doobie.Transactor
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.parser.parse
import ru.tinkoff.domain.Article
import doobie.util.Write
import doobie.util.update.Update
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.util.ExecutionContexts
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.implicits.javatime._

object DownloadSQL {

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

  def insertManyArticle(data: List[Article]) = {
    val sql: String =
      """insert into public."Articles" (create_timestamp, "timestamp", language,
          wiki, category, title, auxiliary_text)
          values (?, ?, ?, ?, ?, ?, ?)"""
    Update[Article](sql)
      .updateMany(data)
  }
}

class DoobieDownloadInterpreter(val xa: Transactor[IO]) {
  implicit val decoderArticle: Decoder[Article] = deriveDecoder[Article]

  def parseContent(content: String): IO[List[Article]] =
    content
      .split("\n")
      .toList
      .traverse(line => IO.fromEither(parse(line)))
      .map(x => x.mapFilter(_.as[Article].toOption))

  def insertDataToArticle(data: List[Article]): IO[Unit] =
    DownloadSQL
      .insertManyArticle(data)
      .transact[IO](xa)
      .map(_ => Unit)
}

object DoobieDownloadInterpreter {
  def apply(xa: Transactor[IO]): DoobieDownloadInterpreter = new DoobieDownloadInterpreter(xa)
}
