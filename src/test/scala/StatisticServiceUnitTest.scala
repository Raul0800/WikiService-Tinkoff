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

  trait TestStatisticService {
    implicit val logger: Logger[IO]               = mock[Logger[IO]]
    val statisticRepo: DoobieStatisticInterpreter = mock[DoobieStatisticInterpreter]

    val statisticService: StatisticService = StatisticService(statisticRepo)(logger)
  }

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  "Statistic" should "not empty List" in {
    val tStatisticService = mock[TestStatisticService]
    (tStatisticService.statisticRepo.statistic _)
      .expects()
      .returning(DoobieStatisticInterpreter(transactor).statistic())

    tStatisticService.statisticService.statistic.map(i => i.isEmpty should be(false))
  }
}
