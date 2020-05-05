package ru.tinkoff.service

import cats.effect.IO
import ru.tinkoff.domain.SearchArticle
import ru.tinkoff.repository.DoobieSearchInterpreter
//import ru.tinkoff.repository.Utils._

class SearchService(repo: DoobieSearchInterpreter) {

  def searchArticle(nameTitle: String): IO[List[SearchArticle]] =
    repo
      .findArticleByTitle(nameTitle)
}

object SearchService {
  def apply(repo: DoobieSearchInterpreter): SearchService = new SearchService(repo)
}
