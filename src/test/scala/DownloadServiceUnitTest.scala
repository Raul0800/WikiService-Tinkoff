import cats.effect.{ExitCode, IO}
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.config.SourceConfig
import ru.tinkoff.repository.DoobieDownloadInterpreter
import ru.tinkoff.service.DownloadService

import scala.concurrent.ExecutionContext

class DownloadServiceUnitTest extends AnyFlatSpec with Matchers with MockFactory {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "postgres"
  )

  "Download records" should "IO(Success)" in {
    val downloadRepo    = DoobieDownloadInterpreter(transactor)
    implicit val logger = mock[Logger[IO]]
    val source          = SourceConfig(List(""))
    val downloadService = DownloadService(downloadRepo, source)
    downloadService.download.map(i => i should be(ExitCode.Success))
  }

  "Parse content string" should "not empty List(Article)" in {
    val str: String =
      "{\"template\":[\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:\\u0417\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\",\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:\\u0417\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\\/styles.css\",\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:Unsigned\",\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:\\u0417\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\\/\"],\"redirect\":[],\"heading\":[\"\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\"],\"source_text\":\"== [[\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440]] ==\\n\\n{{\\u0437\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e}}\\n\\u0412 \\u0441\\u0432\\u044f\\u0437\\u0438 \\u0441 \\u043e\\u0442\\u0441\\u0443\\u0442\\u0441\\u0442\\u0432\\u0438\\u0435\\u043c \\u0448\\u0430\\u0431\\u043b\\u043e\\u043d\\u0430 '''\\u041a \\u041e\\u0431\\u044a\\u0435\\u0434\\u0438\\u043d\\u0435\\u043d\\u0438\\u044e'''. \\u0421 \\u043f\\u0435\\u0440\\u0435\\u043d\\u0435\\u0441\\u0435\\u043d\\u0438\\u0435\\u043c \\u0441\\u043e\\u0434\\u0435\\u0440\\u0436\\u0430\\u043d\\u0438\\u044f \\u0432 \\u0441\\u0442\\u0430\\u0442\\u044c\\u044e [[\\u0410\\u043b\\u044c\\u0431\\u0443\\u0441 \\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440]] {{unsigned|15:48, 19 \\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430 2012|\\u0422\\u0430\\u0431\\u0443\\u0440\\u0435\\u0442\\u043a\\u0430}}\\n\\n\\u0421\\u043f\\u0430\\u0441\\u0438\\u0431\\u043e. --[[\\u0423\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c)|\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c)]] ([[\\u041e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435 \\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u0430:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c)|\\u043e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435]]) 23:57, 11 \\u043d\\u043e\\u044f\\u0431\\u0440\\u044f 2015 (UTC)\\n{{\\u0437\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\\/}}\",\"version_type\":\"external\",\"opening_text\":null,\"wiki\":\"ruwikiquote\",\"auxiliary_text\":[],\"language\":\"ru\",\"title\":\"\\u041a \\u0443\\u0434\\u0430\\u043b\\u0435\\u043d\\u0438\\u044e\\/\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\",\"version\":272810,\"external_link\":[],\"namespace_text\":\"\\u0412\\u0438\\u043a\\u0438\\u0446\\u0438\\u0442\\u0430\\u0442\\u043d\\u0438\\u043a\",\"namespace\":4,\"text_bytes\":567,\"incoming_links\":1,\"text\":\"\\u0417\\u0434\\u0435\\u0441\\u044c \\u043d\\u0430\\u0445\\u043e\\u0434\\u044f\\u0442\\u0441\\u044f \\u0437\\u0430\\u0432\\u0435\\u0440\\u0448\\u0438\\u0432\\u0448\\u0438\\u0435\\u0441\\u044f \\u043e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u044f. \\u041f\\u0440\\u043e\\u0441\\u044c\\u0431\\u0430 \\u043d\\u0435 \\u0432\\u043d\\u043e\\u0441\\u0438\\u0442\\u044c \\u0438\\u0437\\u043c\\u0435\\u043d\\u0435\\u043d\\u0438\\u0439. \\u0412 \\u0441\\u0432\\u044f\\u0437\\u0438 \\u0441 \\u043e\\u0442\\u0441\\u0443\\u0442\\u0441\\u0442\\u0432\\u0438\\u0435\\u043c \\u0448\\u0430\\u0431\\u043b\\u043e\\u043d\\u0430 \\u041a \\u041e\\u0431\\u044a\\u0435\\u0434\\u0438\\u043d\\u0435\\u043d\\u0438\\u044e. \\u0421 \\u043f\\u0435\\u0440\\u0435\\u043d\\u0435\\u0441\\u0435\\u043d\\u0438\\u0435\\u043c \\u0441\\u043e\\u0434\\u0435\\u0440\\u0436\\u0430\\u043d\\u0438\\u044f \\u0432 \\u0441\\u0442\\u0430\\u0442\\u044c\\u044e \\u0410\\u043b\\u044c\\u0431\\u0443\\u0441 \\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440 \\u2014\\u00a0\\u042d\\u0442\\u0430 \\u0440\\u0435\\u043f\\u043b\\u0438\\u043a\\u0430 \\u0434\\u043e\\u0431\\u0430\\u0432\\u043b\\u0435\\u043d\\u0430 \\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u043e\\u043c 15:48, 19 \\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430 2012 (\\u043e \\u00b7 \\u0432) \\u0422\\u0430\\u0431\\u0443\\u0440\\u0435\\u0442\\u043a\\u0430 \\u0421\\u043f\\u0430\\u0441\\u0438\\u0431\\u043e. --\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c) (\\u043e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435) 23:57, 11 \\u043d\\u043e\\u044f\\u0431\\u0440\\u044f 2015 (UTC)\",\"category\":[],\"outgoing_link\":[\"\\u0423\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f_\\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b_(\\u0414\\u041c)\",\"\\u0423\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a:15:48,_19_\\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430_2012\",\"\\u0410\\u043b\\u044c\\u0431\\u0443\\u0441_\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\",\"\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\",\"\\u041e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435_\\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u0430:15:48,_19_\\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430_2012\",\"\\u041e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435_\\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u0430:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f_\\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b_(\\u0414\\u041c)\"],\"timestamp\":\"2015-11-12T08:23:34Z\",\"content_model\":\"wikitext\",\"create_timestamp\":\"2012-08-19T11:48:48Z\",\"defaultsort\":false}"
    val downloadRepo = DoobieDownloadInterpreter(transactor)
    downloadRepo.parseContent(str).map(l => l.isEmpty should be(false))
  }

  "Insert data to article" should "IO[Unit]" in {
    val str: String =
      "{\"template\":[\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:\\u0417\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\",\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:\\u0417\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\\/styles.css\",\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:Unsigned\",\"\\u0428\\u0430\\u0431\\u043b\\u043e\\u043d:\\u0417\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\\/\"],\"redirect\":[],\"heading\":[\"\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\"],\"source_text\":\"== [[\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440]] ==\\n\\n{{\\u0437\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e}}\\n\\u0412 \\u0441\\u0432\\u044f\\u0437\\u0438 \\u0441 \\u043e\\u0442\\u0441\\u0443\\u0442\\u0441\\u0442\\u0432\\u0438\\u0435\\u043c \\u0448\\u0430\\u0431\\u043b\\u043e\\u043d\\u0430 '''\\u041a \\u041e\\u0431\\u044a\\u0435\\u0434\\u0438\\u043d\\u0435\\u043d\\u0438\\u044e'''. \\u0421 \\u043f\\u0435\\u0440\\u0435\\u043d\\u0435\\u0441\\u0435\\u043d\\u0438\\u0435\\u043c \\u0441\\u043e\\u0434\\u0435\\u0440\\u0436\\u0430\\u043d\\u0438\\u044f \\u0432 \\u0441\\u0442\\u0430\\u0442\\u044c\\u044e [[\\u0410\\u043b\\u044c\\u0431\\u0443\\u0441 \\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440]] {{unsigned|15:48, 19 \\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430 2012|\\u0422\\u0430\\u0431\\u0443\\u0440\\u0435\\u0442\\u043a\\u0430}}\\n\\n\\u0421\\u043f\\u0430\\u0441\\u0438\\u0431\\u043e. --[[\\u0423\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c)|\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c)]] ([[\\u041e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435 \\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u0430:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c)|\\u043e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435]]) 23:57, 11 \\u043d\\u043e\\u044f\\u0431\\u0440\\u044f 2015 (UTC)\\n{{\\u0437\\u0430\\u043a\\u0440\\u044b\\u0442\\u043e\\/}}\",\"version_type\":\"external\",\"opening_text\":null,\"wiki\":\"ruwikiquote\",\"auxiliary_text\":[],\"language\":\"ru\",\"title\":\"\\u041a \\u0443\\u0434\\u0430\\u043b\\u0435\\u043d\\u0438\\u044e\\/\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\",\"version\":272810,\"external_link\":[],\"namespace_text\":\"\\u0412\\u0438\\u043a\\u0438\\u0446\\u0438\\u0442\\u0430\\u0442\\u043d\\u0438\\u043a\",\"namespace\":4,\"text_bytes\":567,\"incoming_links\":1,\"text\":\"\\u0417\\u0434\\u0435\\u0441\\u044c \\u043d\\u0430\\u0445\\u043e\\u0434\\u044f\\u0442\\u0441\\u044f \\u0437\\u0430\\u0432\\u0435\\u0440\\u0448\\u0438\\u0432\\u0448\\u0438\\u0435\\u0441\\u044f \\u043e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u044f. \\u041f\\u0440\\u043e\\u0441\\u044c\\u0431\\u0430 \\u043d\\u0435 \\u0432\\u043d\\u043e\\u0441\\u0438\\u0442\\u044c \\u0438\\u0437\\u043c\\u0435\\u043d\\u0435\\u043d\\u0438\\u0439. \\u0412 \\u0441\\u0432\\u044f\\u0437\\u0438 \\u0441 \\u043e\\u0442\\u0441\\u0443\\u0442\\u0441\\u0442\\u0432\\u0438\\u0435\\u043c \\u0448\\u0430\\u0431\\u043b\\u043e\\u043d\\u0430 \\u041a \\u041e\\u0431\\u044a\\u0435\\u0434\\u0438\\u043d\\u0435\\u043d\\u0438\\u044e. \\u0421 \\u043f\\u0435\\u0440\\u0435\\u043d\\u0435\\u0441\\u0435\\u043d\\u0438\\u0435\\u043c \\u0441\\u043e\\u0434\\u0435\\u0440\\u0436\\u0430\\u043d\\u0438\\u044f \\u0432 \\u0441\\u0442\\u0430\\u0442\\u044c\\u044e \\u0410\\u043b\\u044c\\u0431\\u0443\\u0441 \\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440 \\u2014\\u00a0\\u042d\\u0442\\u0430 \\u0440\\u0435\\u043f\\u043b\\u0438\\u043a\\u0430 \\u0434\\u043e\\u0431\\u0430\\u0432\\u043b\\u0435\\u043d\\u0430 \\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u043e\\u043c 15:48, 19 \\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430 2012 (\\u043e \\u00b7 \\u0432) \\u0422\\u0430\\u0431\\u0443\\u0440\\u0435\\u0442\\u043a\\u0430 \\u0421\\u043f\\u0430\\u0441\\u0438\\u0431\\u043e. --\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f \\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b (\\u0414\\u041c) (\\u043e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435) 23:57, 11 \\u043d\\u043e\\u044f\\u0431\\u0440\\u044f 2015 (UTC)\",\"category\":[],\"outgoing_link\":[\"\\u0423\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f_\\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b_(\\u0414\\u041c)\",\"\\u0423\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a:15:48,_19_\\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430_2012\",\"\\u0410\\u043b\\u044c\\u0431\\u0443\\u0441_\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\",\"\\u0414\\u0430\\u043c\\u0431\\u043b\\u0434\\u043e\\u0440\",\"\\u041e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435_\\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u0430:15:48,_19_\\u0430\\u0432\\u0433\\u0443\\u0441\\u0442\\u0430_2012\",\"\\u041e\\u0431\\u0441\\u0443\\u0436\\u0434\\u0435\\u043d\\u0438\\u0435_\\u0443\\u0447\\u0430\\u0441\\u0442\\u043d\\u0438\\u043a\\u0430:\\u0411\\u0440\\u0430\\u0442\\u044c\\u044f_\\u0421\\u0442\\u043e\\u044f\\u043b\\u043e\\u0432\\u044b_(\\u0414\\u041c)\"],\"timestamp\":\"2015-11-12T08:23:34Z\",\"content_model\":\"wikitext\",\"create_timestamp\":\"2012-08-19T11:48:48Z\",\"defaultsort\":false}"
    val downloadRepo = DoobieDownloadInterpreter(transactor)
    downloadRepo
      .parseContent(str)
      .map { l =>
        downloadRepo.insertDataToArticle(l).map(u => u should be(Unit))
      }
  }
}