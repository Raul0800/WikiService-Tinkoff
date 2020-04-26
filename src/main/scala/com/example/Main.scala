package com.example

import com.example.endpoint.WikiEndpoint
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig._
import pureconfig.generic.auto._

object Main extends IOApp {

  final case class ConfigService(port: Int, host: String)

  val confService = ConfigSource.default.loadOrThrow[ConfigService]

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(confService.port, confService.host)
      .withHttpApp(new WikiEndpoint().routes.orNotFound)
      .serve
      .compile
      .drain
      .map(_ => ExitCode.Success)
}
