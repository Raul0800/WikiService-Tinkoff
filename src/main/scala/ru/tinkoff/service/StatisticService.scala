package ru.tinkoff.service

import cats.effect.IO
import ru.tinkoff.repository.DoobieStatisticInterpreter
import io.chrisdavenport.log4cats.Logger
import ru.tinkoff.domain.Catalog

class StatisticService(repo: DoobieStatisticInterpreter)(
    implicit logger: Logger[IO]
) {

  def statistic: IO[List[Catalog]] =
    repo
      .statistic()
      .handleErrorWith { mes =>
        logger
          .warn(mes)(s"Statistic. Some problems")
          .map(_ => List.empty)
      }
}

object StatisticService {

  def apply(repo: DoobieStatisticInterpreter)(
      implicit logger: Logger[IO]
  ): StatisticService = new StatisticService(repo)
}
