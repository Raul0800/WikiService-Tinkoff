//import cats.effect.IO
//import doobie.util.transactor.Transactor
//import org.scalatest._
//
//import scala.concurrent.ExecutionContext
//
//import ru.tinkoff.repository.ContainerRepository._
//
//class AnalysisTestDatabase extends FunSuite with Matchers with doobie.scalatest.IOChecker {
//  override val colors = doobie.util.Colors.None // just for docs
//  implicit val cs     = IO.contextShift(ExecutionContext.global)
//
//  val transactor = Transactor.fromDriverManager[IO](
//    "org.postgresql.Driver",
//    "jdbc:postgresql://localhost:5432/postgres",
//    "postgres",
//    "postgres"
//  )
//
//  test("find") { check(find("Alexsmail")) }
//  test("getCatalog") { check(getCatalog) }
//}
//
//object StartTest extends App {
//  (new AnalysisTestDatabase).execute(color = false)
//}
