package ru.tinkoff.service

import java.io.File

import cats.effect.{ExitCode, IO}
import fs2.Stream
import ru.tinkoff.config.SourceConfig
import ru.tinkoff.repository.DoobieDownloadInterpreter
import io.chrisdavenport.log4cats.Logger

class DownloadService(repo: DoobieDownloadInterpreter, source: SourceConfig)(
    implicit logger: Logger[IO]
) {

  def download: IO[ExitCode] =
    Stream
      .emits {
        source.paths
          .flatMap(
            name => SourceConfig.getFiles(new File(name))
          )
      }
      .covary[IO]
      .evalMap { file =>
        for {
          _ <- logger.info(s"File name: ${file.getName} start reading")
          content <- SourceConfig
                      .readContent(file)
                      .handleErrorWith { mes =>
                        logger
                          .warn(mes)(s"Read content. Some problems: file - ${file.getName}")
                          .map(_ => "")
                      }
          data <- repo
                   .parseContent(content)
                   .handleErrorWith { mes =>
                     logger
                       .warn(mes)(s"Parse content. Some problems: file - ${file.getName}")
                       .map(_ => List.empty)
                   }
          _ <- repo
                .insertDataToArticle(data)
                .handleErrorWith { mes =>
                  logger
                    .warn(mes)(s"Insert data to DB. Some problems: file - ${file.getName}")
                    .map(identity)
                }
          _ <- logger.info(s"File name: ${file.getName} finish reading")
        } yield ExitCode.Success
      }
      .compile
      .lastOrError
}

object DownloadService {

  def apply(repo: DoobieDownloadInterpreter, source: SourceConfig)(
      implicit logger: Logger[IO]
  ): DownloadService = new DownloadService(repo, source)
}
