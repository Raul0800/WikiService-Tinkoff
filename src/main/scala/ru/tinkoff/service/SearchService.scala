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
      .handleErrorWith { _ =>
        logger
          .warn(s"Search article. Some problems: title - $nameTitle")
          .unsafeRunSync() //TODO??? как быть, чтобы так не делать
        IO(List.empty)
      }
}

object SearchService {

  def apply(repo: DoobieSearchInterpreter)(
      implicit logger: Logger[IO]
  ): SearchService = new SearchService(repo)
}
