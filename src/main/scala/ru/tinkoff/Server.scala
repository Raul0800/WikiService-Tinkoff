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
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.generic.semiauto.deriveReader
import pureconfig.module.catseffect.syntax._
import ru.tinkoff.repository.{DoobieDownloadInterpreter, DoobieSearchInterpreter, DoobieStatisticRepository}
import ru.tinkoff.service.{DownloadService, SearchService, StatisticService}
import io.chrisdavenport.log4cats.Logger

object Server extends IOApp {
  private val config = ConfigFactory.load()

  def createServer(
      implicit logger: Logger[IO]
  ): Resource[IO, Server4s[IO]] =
    for {
      conf             <- Resource.liftF(ConfigSource.fromConfig(config).at("wiki-service").loadF[IO, WikiServiceConfig])
      connEc           <- ExecutionContexts.fixedThreadPool[IO](10)
      txnEc            <- ExecutionContexts.cachedThreadPool[IO]
      xa               <- DatabaseConfig.dbTransactor[IO](conf.db, connEc, Blocker.liftExecutionContext(txnEc))
      _                <- Resource.liftF(DatabaseConfig.initializeDb[IO](conf.db))
      searchRepo       = DoobieSearchInterpreter(xa)
      downloadRepo     = DoobieDownloadInterpreter(xa)
      statisticRepo    = DoobieStatisticRepository(xa)
      searchService    = SearchService(searchRepo)
      downloadService  = DownloadService(downloadRepo, conf.source)
      statisticService = StatisticService(statisticRepo)
      server <- BlazeServerBuilder[IO]
                 .bindHttp(conf.server.port, conf.server.host)
                 .withHttpApp(
                   WikiEndpoint
                     .route(
                       searchService,
                       downloadService,
                       statisticService
                     )
                     .orNotFound
                 )
                 .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    Slf4jLogger.create[IO].flatMap { implicit logger =>
      createServer.use(_ => IO.never)
    }
}
