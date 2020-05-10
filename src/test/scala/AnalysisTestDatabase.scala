import cats.effect.IO
import doobie.util.transactor.Transactor
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext
import ru.tinkoff.repository._

class AnalysisTestDatabase extends AnyFunSuite with Matchers with doobie.scalatest.IOChecker {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  test("Search") { check(SearchSQL.find("")) }
  test("Delete") { check(DeleteSQL.deleteRecord("")) }
}

object StartTest extends App {
  (new AnalysisTestDatabase).execute(color = false)
}
