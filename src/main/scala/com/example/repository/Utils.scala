package com.example.repository

import ContainerRepository._
import java.nio.file.{Files, Path}

import doobie._
import io.circe.parser.parse
import io.circe.generic.semiauto._
import doobie.implicits._
import cats.implicits._
import cats.effect._
import com.example.domain.{Article, SearchArticle}
import io.circe.Decoder
import pureconfig._
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext

sealed trait Config

final case class DatabaseConfig(url: String, driver: String, userName: String, password: String) extends Config

final case class SourceConfig(fileName: String) extends Config

object Utils {

  implicit val cs                               = IO.contextShift(ExecutionContext.global)
  implicit val decoderArticle: Decoder[Article] = deriveDecoder[Article]

  lazy val cfgDb     = ConfigSource.default.loadOrThrow[DatabaseConfig]
  lazy val cfgSourse = ConfigSource.default.loadOrThrow[SourceConfig]

  private def xa = Transactor.fromDriverManager[IO](
    cfgDb.driver,
    cfgDb.url,
    cfgDb.userName,
    cfgDb.password
  )

  def readContent(): IO[String] =
    IO(new String(Files.readAllBytes(Path.of(cfgSourse.fileName))))

  def parseContent(content: String): IO[List[Article]] =
    content
      .split("\n")
      .toList
      .traverse(line => IO.fromEither(parse(line)))
      .map(x => x.mapFilter(_.as[Article].toOption))

  def initializeDb(): IO[Unit] =
    (dropArticle, dropCatalog, createArticle, createCatalog)
      .mapN(_ + _ + _ + _)
      .transact[IO](xa)
      .map(_ => Unit)

  def insertDataToArticle(data: List[Article]): IO[Unit] =
    insertManyArticle(data)
      .transact[IO](xa)
      .map(_ => Unit)

  def insertDataToCatalog() =
    insertCatalog
      .transact[IO](xa)
      .map(_ => Unit)

  def createReferenceArticleCatalog() =
    createReference
      .transact[IO](xa)
      .map(_ => Unit)

  def createIndexTitle() =
    createIndTitle
      .transact[IO](xa)
      .map(_ => Unit)

  def updateArticle() =
    updArticle
      .transact[IO](xa)
      .map(_ => Unit)

  def findArticleByTitle(title: String): IO[List[SearchArticle]] =
    find(title)
      .to[List]
      .transact[IO](xa)

  def getCatalogInfo() =
    getCatalog
      .to[List]
      .transact[IO](xa)
}
