import cats.effect.{ContextShift, IO}
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import io.chrisdavenport.log4cats.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.repository.DoobieDeleteInterpreter
import ru.tinkoff.service.DeleteService

import scala.concurrent.ExecutionContext

class DeleteServiceUnitTest extends AnyFlatSpec with Matchers with MockFactory {

  trait TestDeleteService {
    implicit val logger: Logger[IO]      = mock[Logger[IO]]
    val delRepo: DoobieDeleteInterpreter = mock[DoobieDeleteInterpreter]

    val delService: DeleteService = DeleteService(delRepo)(logger)
  }

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val transactor: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  "Delete one record with name: Abracadabra" should "0" in {
    val tDelService = mock[TestDeleteService]
    (tDelService.delRepo.deleteRecord _)
      .expects("Abracadabra")
      .returning(DoobieDeleteInterpreter(transactor).deleteRecord("Abracadabra"))
    tDelService.delService.deleteRecordByTitle("Abracadabra").map(i => i should be(0))
  }

  "Delete one record with name: Abracadabra, but repo with Fail" should "-1" in {
    val tDelService = mock[TestDeleteService]
    (tDelService.delRepo.deleteRecord _).expects("Abracadabra").returning(IO.raiseError(new Exception))
    tDelService.delService.deleteRecordByTitle("Abracadabra").map(i => i should be(-1))
  }
}
