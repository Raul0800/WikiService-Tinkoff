import cats.effect.{ExitCode, IO}
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.config.SourceConfig
import ru.tinkoff.repository.DoobieSearchInterpreter
import ru.tinkoff.service.SearchService

import scala.concurrent.ExecutionContext

class SearchServiceUnitTest extends AnyFlatSpec with Matchers with MockFactory {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  "Search records with title: Qwerty" should "List.empty" in {
    val searchRepo      = DoobieSearchInterpreter(transactor)
    implicit val logger = mock[Logger[IO]]

    val searchService = SearchService(searchRepo)
    searchService.searchArticle("Qwerty").map(i => i should be(List.empty))
  }

  "Search records with title: Бразилия" should "List(Articles)" in {
    val searchRepo      = DoobieSearchInterpreter(transactor)
    implicit val logger = mock[Logger[IO]]

    val searchService = SearchService(searchRepo)
    val s             = searchService.searchArticle("Бразилия")
    s.map(i => i.isEmpty should be(false))

  }
}
