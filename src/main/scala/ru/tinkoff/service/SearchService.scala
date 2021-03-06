package ru.tinkoff.service

import cats.effect.IO
import ru.tinkoff.domain.SearchArticle
import ru.tinkoff.repository.DoobieSearchInterpreter
import io.chrisdavenport.log4cats.Logger

class SearchService(repo: DoobieSearchInterpreter)(
    implicit logger: Logger[IO]
) {

  def searchArticle(nameTitle: String): IO[List[SearchArticle]] =
    repo
      .findArticleByTitle(nameTitle)
      .handleErrorWith { mes =>
        logger
          .warn(mes)(s"Search article. Some problems: title - $nameTitle")
          .flatMap(_ => IO.raiseError(new RuntimeException))
      }
}

object SearchService {

  def apply(repo: DoobieSearchInterpreter)(
      implicit logger: Logger[IO]
  ): SearchService = new SearchService(repo)
}
