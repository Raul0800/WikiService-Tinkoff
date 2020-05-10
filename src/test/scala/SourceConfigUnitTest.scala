import java.io.File

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.config.SourceConfig

class SourceConfigUnitTest extends AnyFlatSpec with Matchers with MockFactory {
  "SourceConfig(List(1, 2, 3))" should "SourceConfig(path: List[String] = List(1, 2, 3))" in {
    val sc = SourceConfig(List("1", "2", "3"))

    sc.paths should be(Seq("1", "2", "3"))
  }
}
