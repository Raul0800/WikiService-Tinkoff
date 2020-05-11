package ru.tinkoff.service

import cats.effect.IO
import io.chrisdavenport.log4cats.Logger
import ru.tinkoff.repository.DoobieDeleteInterpreter

class DeleteService(repo: DoobieDeleteInterpreter)(
    implicit logger: Logger[IO]
) {

  def deleteRecordByTitle(title: String): IO[Int] =
    repo
      .deleteRecord(title)
      .handleErrorWith(
        mes =>
          logger
            .warn(mes)(s"Delete article. Some problems: title - $title")
            .map(_ => -1)
      )
}

object DeleteService {

  def apply(repo: DoobieDeleteInterpreter)(
      implicit logger: Logger[IO]
  ): DeleteService = new DeleteService(repo)
}
