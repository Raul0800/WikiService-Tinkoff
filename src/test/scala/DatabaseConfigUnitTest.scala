import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.config.DatabaseConfig

class DatabaseConfigUnitTest extends AnyFlatSpec with Matchers {
  "dbconfig(a, b, c, d)" should "DatabaseConfig(url: String = a, driver: String = b, userName = c: String, password: String = d)" in {
    val dbconfig = DatabaseConfig(url = "a", driver = "b", userName = "c", password = "d")

    dbconfig.url should be("a")
    dbconfig.driver should be("b")
    dbconfig.userName should be("c")
    dbconfig.password should be("d")
  }
}
