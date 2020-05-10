package ru.tinkoff.repository

import cats.effect.IO
import doobie.Transactor
import doobie.util.Read
import doobie.implicits._
import doobie.postgres.implicits._
import ru.tinkoff.domain.Catalog

object StatisticSQL {

  implicit val catalogRead: Read[Catalog] =
    Read[(Array[String], Int)].map { case (a, b) => Catalog(a, b) }

  def getCatalog: doobie.Query0[Catalog] =
    sql"""
        SELECT category, count
	        FROM public."Statistic";
           """
      .query[Catalog]
}

class DoobieStatisticInterpreter(val xa: Transactor[IO]) {

  def statistic(): IO[List[Catalog]] =
    StatisticSQL.getCatalog
      .to[List]
      .transact[IO](xa)
}

object DoobieStatisticInterpreter {
  def apply(xa: Transactor[IO]): DoobieStatisticInterpreter = new DoobieStatisticInterpreter(xa)
}
