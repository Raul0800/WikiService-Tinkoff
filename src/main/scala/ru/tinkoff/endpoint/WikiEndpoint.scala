package ru.tinkoff.endpoint

import cats.effect.IO
import cats.implicits.{toFoldableOps => _}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.jsonEncoder
import org.http4s.dsl.Http4sDsl
import ru.tinkoff.service.{DownloadService, SearchService}

object WikiEndpoint extends Http4sDsl[IO] {

  def route(searchService: SearchService, downloadService: DownloadService): HttpRoutes[IO] = HttpRoutes.of[IO] {
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
  }
//  def routes(str: String): HttpRoutes[IO] = HttpRoutes.of[IO] {
//    case GET -> Root / "wiki" / nameTitle :? command => {
//      SearchService
//        .searchArticle(nameTitle)
//        .flatMap(
//          x =>
//            command.keySet match {
//              case a if a.isEmpty =>
//                Ok(x.asJson.noSpaces.replace(System.lineSeparator(), ""))
//              case a if a.contains("pretty") =>
//                Ok(x.asJson)
//              case _ =>
//                BadRequest("Unknown command")
//            }
//        )
//        .handleErrorWith {
//          case e: SearchService => BadRequest(e.getClass.getName)
//          case _: Throwable     => BadRequest("Unexpected error")
//        }
//    }
//
//    case GET -> Root / "initialize" => {
//      SearchService.initialize
//        .flatMap(_ => Ok("Done"))
//        .handleErrorWith {
//          case e: SearchService => BadRequest(e.getClass.getName)
//          case _: Throwable     => BadRequest("Unexpected error")
//        }
//    }
//
//    case GET -> Root / "statistic" => {
//      SearchService.statistic
//        .flatMap(x => Ok(x.asJson))
//        .handleErrorWith {
//          case e: SearchService => BadRequest(e.getClass.getName)
//          case _: Throwable     => BadRequest("Unexpected error")
//        }
//    }
//  }
}
