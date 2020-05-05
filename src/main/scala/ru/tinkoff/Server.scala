package ru.tinkoff

import cats.effect._
import config._
import org.http4s.implicits._
import org.http4s.server.{Server => Server4s}
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig._
import pureconfig.generic.auto._
import ru.tinkoff.config.{DatabaseConfig, WikiServiceConfig}
import ru.tinkoff.endpoint.WikiEndpoint
import pureconfig.module.catseffect.loadConfigF
import com.typesafe.config.{Config, ConfigFactory}
import doobie.util.ExecutionContexts
import pureconfig.generic.semiauto.deriveReader
import pureconfig.module.catseffect.syntax._
import ru.tinkoff.repository.{DoobieDownloadInterpreter, DoobieSearchInterpreter}
import ru.tinkoff.service.{DownloadService, SearchService}

object Server extends IOApp {
  private val config = ConfigFactory.load()

  def createServer: Resource[IO, Server4s[IO]] =
    for {
      conf            <- Resource.liftF(ConfigSource.fromConfig(config).at("wiki-service").loadF[IO, WikiServiceConfig])
      connEc          <- ExecutionContexts.fixedThreadPool[IO](10)
      txnEc           <- ExecutionContexts.cachedThreadPool[IO]
      xa              <- DatabaseConfig.dbTransactor[IO](conf.db, connEc, Blocker.liftExecutionContext(txnEc))
      _               <- Resource.liftF(DatabaseConfig.initializeDb[IO](conf.db))
      searchRepo      = DoobieSearchInterpreter(xa)
      downloadRepo    = DoobieDownloadInterpreter(xa)
      searchService   = SearchService(searchRepo)
      downloadService = DownloadService(downloadRepo, conf.source)
      server <- BlazeServerBuilder[IO]
                 .bindHttp(conf.server.port, conf.server.host)
                 .withHttpApp(WikiEndpoint.route(searchService, downloadService).orNotFound)
                 .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never)
}
