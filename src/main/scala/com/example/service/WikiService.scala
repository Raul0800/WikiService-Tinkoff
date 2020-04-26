package com.example.service

import cats.effect.IO
import com.example.repository.Utils._

sealed trait WikiService extends Throwable

case object CannotInitDB extends WikiService

case object CannotReadSource extends WikiService

case object SourceIsCorrupted extends WikiService

case object CannotInsertData extends WikiService

case object CannotCreateReference extends WikiService

case object CannotUpdateData extends WikiService

case object CannotCreateIndex extends WikiService

case object CannotGetStatistic extends WikiService

case object SearchError extends WikiService

object WikiService {

  lazy val initialize = {
    for {
      _ <- initializeDb
            .handleErrorWith(_ => IO.raiseError(CannotInitDB))
      content <- readContent
                  .handleErrorWith(_ => IO.raiseError(CannotReadSource))
      data <- parseContent(content)
               .handleErrorWith(_ => IO.raiseError(SourceIsCorrupted))
      _ <- insertDataToArticle(data)
            .handleErrorWith(_ => IO.raiseError(CannotInsertData))
      _ <- createIndexTitle
            .handleErrorWith(_ => IO.raiseError(CannotCreateIndex))
      _ <- insertDataToCatalog()
            .handleErrorWith(_ => IO.raiseError(CannotInsertData))
      _ <- createReferenceArticleCatalog()
            .handleErrorWith(_ => IO.raiseError(CannotCreateReference))
      _ <- updateArticle()
            .handleErrorWith(_ => IO.raiseError(CannotUpdateData))
    } yield ()
  }

  lazy val statistic = {
    getCatalogInfo
      .handleErrorWith(_ => IO.raiseError(CannotGetStatistic))
  }

  def searchArticle(nameTitle: String) =
    findArticleByTitle(nameTitle)
      .handleErrorWith(_ => IO.raiseError(SearchError))
}
