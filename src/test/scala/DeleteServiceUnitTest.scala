import cats.effect.IO
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.repository.DoobieDeleteInterpreter
import ru.tinkoff.service.DeleteService

import scala.concurrent.ExecutionContext

class DeleteServiceUnitTest extends AnyFlatSpec with Matchers with MockFactory {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  "Delete one record with name: Abracadabra" should "0" in {
    val delRepo         = DoobieDeleteInterpreter(transactor)
    implicit val logger = mock[Logger[IO]]
    val delService      = DeleteService(delRepo)
    delService.deleteRecordByTitle("Abracadabra").map(i => i should be(0))
  }

  "Delete one record with name: Abracadabra, but repo with Fail" should "-1" in {
    val delRepo = mock[DoobieDeleteInterpreter]
    (delRepo.deleteRecord _).expects("Abracadabra").returning(IO.raiseError(new Exception))
    implicit val logger = mock[Logger[IO]]
    val delService      = DeleteService(delRepo)
    delService.deleteRecordByTitle("Abracadabra").map(i => i should be(-1))
  }
}
