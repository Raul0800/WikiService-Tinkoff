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

  def getCatalog =
    sql"""
        SELECT category, count
	        FROM public."Statistic";
           """
      .query[Catalog]
}

class DoobieStatisticRepository(val xa: Transactor[IO]) {

  def statistic() =
    StatisticSQL.getCatalog
      .to[List]
      .transact[IO](xa)
}

object DoobieStatisticRepository {
  def apply(xa: Transactor[IO]): DoobieStatisticRepository = new DoobieStatisticRepository(xa)
}
