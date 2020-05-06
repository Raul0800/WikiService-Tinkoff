package ru.tinkoff.endpoint

import cats.effect.IO
import cats.implicits.{toFoldableOps => _}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.jsonEncoder
import org.http4s.dsl.Http4sDsl
import ru.tinkoff.service.{DownloadService, SearchService, StatisticService}

object WikiEndpoint extends Http4sDsl[IO] {

  def route(
      searchService: SearchService,
      downloadService: DownloadService,
      statisticService: StatisticService
  ): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "wiki" / nameTitle =>
      searchService
        .searchArticle(nameTitle)
        .flatMap(req => Ok(req.asJson))
        .handleErrorWith(_ => InternalServerError("Probably database problems"))

    case GET -> Root / "download" => {
      downloadService.download
        .flatMap(req => Ok(req.asJson))
        .handleErrorWith(_ => InternalServerError("Check paths, file availability, database connection"))
    }

    case GET -> Root / "statistic" => {
      statisticService.statistic
        .flatMap(x => Ok(x.asJson))
        .handleErrorWith(_ => InternalServerError())
    }
  }
}
