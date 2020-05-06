package ru.tinkoff.service

import cats.effect.IO
import ru.tinkoff.repository.DoobieStatisticRepository
import io.chrisdavenport.log4cats.Logger
import ru.tinkoff.domain.Catalog

class StatisticService(repo: DoobieStatisticRepository)(
    implicit logger: Logger[IO]
) {

  def statistic: IO[List[Catalog]] =
    repo
      .statistic()
      .handleErrorWith { _ =>
        logger.warn(s"Statistic. Some problems")
        IO(List.empty)
      }
}

object StatisticService {

  def apply(repo: DoobieStatisticRepository)(
      implicit logger: Logger[IO]
  ): StatisticService = new StatisticService(repo)
}
