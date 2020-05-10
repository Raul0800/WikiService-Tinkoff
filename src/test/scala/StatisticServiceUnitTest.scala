import cats.effect.{ExitCode, IO}
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.config.SourceConfig
import ru.tinkoff.repository.DoobieStatisticInterpreter
import ru.tinkoff.service.StatisticService

import scala.concurrent.ExecutionContext

class StatisticServiceUnitTest extends AnyFlatSpec with Matchers with MockFactory {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  "Statistic" should "not empty List" in {
    val statisticRepo   = DoobieStatisticInterpreter(transactor)
    implicit val logger = mock[Logger[IO]]

    val statisticService = StatisticService(statisticRepo)
    statisticService.statistic.map(i => i.isEmpty should be(false))
  }
}
