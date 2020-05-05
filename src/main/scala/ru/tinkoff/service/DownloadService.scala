package ru.tinkoff.service

import java.io.File

import cats.effect.IO
import fs2.Stream
import ru.tinkoff.config.SourceConfig
import ru.tinkoff.repository.DoobieDownloadInterpreter

class DownloadService(repo: DoobieDownloadInterpreter, source: SourceConfig) {

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
                content <- SourceConfig.readContent(file)
                data    <- repo.parseContent(content)
                _       <- repo.insertDataToArticle(data)
              } yield ()
            }
            .compile
            .drain
    } yield ()
}

object DownloadService {
  def apply(repo: DoobieDownloadInterpreter, source: SourceConfig): DownloadService = new DownloadService(repo, source)
}
