package com.example.endpoint

import cats.effect.IO
import cats.implicits.{toFoldableOps => _}
import com.example.repository.Utils._
import com.example.service.WikiService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.jsonEncoder
import org.http4s.dsl.Http4sDsl

class WikiEndpoint() extends Http4sDsl[IO] {

  def routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "wiki" / nameTitle :? command => {
      WikiService
        .searchArticle(nameTitle)
        .flatMap(
          x =>
            command.keySet match {
              case a if a.isEmpty =>
                Ok(x.asJson.noSpaces.replace(System.lineSeparator(), ""))
              case a if a.contains("pretty") =>
                Ok(x.asJson)
              case _ =>
                BadRequest("Unknown command")
            }
        )
        .handleErrorWith {
          case e: WikiService => BadRequest(e.getClass.getName)
          case _: Throwable   => BadRequest("Unexpected error")
        }
    }

    case GET -> Root / "initialize" => {
      WikiService.initialize
        .flatMap(_ => Ok("Done"))
        .handleErrorWith {
          case e: WikiService => BadRequest(e.getClass.getName)
          case _: Throwable   => BadRequest("Unexpected error")
        }
    }

    case GET -> Root / "statistic" => {
      WikiService.statistic
        .flatMap(x => Ok(x.asJson))
        .handleErrorWith {
          case e: WikiService => BadRequest(e.getClass.getName)
          case _: Throwable   => BadRequest("Unexpected error")
        }
    }
  }
}
