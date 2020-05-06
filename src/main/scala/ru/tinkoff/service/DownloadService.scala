package ru.tinkoff.service

import java.io.File

import cats.effect.IO
import fs2.Stream
import ru.tinkoff.config.SourceConfig
import ru.tinkoff.repository.DoobieDownloadInterpreter
import io.chrisdavenport.log4cats.Logger

class DownloadService(repo: DoobieDownloadInterpreter, source: SourceConfig)(
    implicit logger: Logger[IO]
) {

  def download: IO[Unit] =
    for {
      _ <- Stream
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
                            .handleErrorWith { _ =>
                              logger.warn(s"Read content. Some problems: file - ${file.getName}")
                              IO("")
                            }
                data <- repo
                         .parseContent(content)
                         .handleErrorWith { _ =>
                           logger.warn(s"Parse content. Some problems: file - ${file.getName}")
                           IO(List.empty)
                         }
                _ <- repo
                      .insertDataToArticle(data)
                      .handleErrorWith { _ =>
                        logger.warn(s"Insert data to DB. Some problems: file - ${file.getName}")
                      }
                _ <- logger.info(s"File name: ${file.getName} finish reading")
              } yield ()
            }
            .compile
            .drain
    } yield ()
}

object DownloadService {

  def apply(repo: DoobieDownloadInterpreter, source: SourceConfig)(
      implicit logger: Logger[IO]
  ): DownloadService = new DownloadService(repo, source)
}
