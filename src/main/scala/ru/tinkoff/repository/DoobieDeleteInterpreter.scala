package ru.tinkoff.repository

import cats.effect.IO
import doobie.Transactor
import doobie.implicits._

object DeleteSQL {

  def deleteRecord(title: String): doobie.Update0 =
    sql"""delete from public."Articles" where title like $title;""".update

}

class DoobieDeleteInterpreter(val xa: Transactor[IO]) {

  def deleteRecord(title: String): IO[Int] =
    DeleteSQL
      .deleteRecord(title)
      .run
      .transact[IO](xa)
}

object DoobieDeleteInterpreter {
  def apply(xa: Transactor[IO]): DoobieDeleteInterpreter = new DoobieDeleteInterpreter(xa)
}
