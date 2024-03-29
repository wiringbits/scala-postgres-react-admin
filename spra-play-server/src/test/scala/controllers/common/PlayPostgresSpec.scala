package controllers.common

import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForEach
import net.wiringbits.spra.api.AdminDataExplorerApiClient
import org.scalatest.TestData
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import org.testcontainers.utility.DockerImageName
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration, Environment, Mode}
import sttp.client3.SttpBackend

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

trait PlayPostgresSpec extends PlayAPISpec with TestContainerForEach with GuiceOneServerPerTest {
  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(30.seconds, 1.second)

  private val postgresImage = DockerImageName.parse("postgres:13")
  override val containerDef: PostgreSQLContainer.Def = PostgreSQLContainer.Def(dockerImageName = postgresImage)

  /** Loads configuration disabling evolutions on default database.
    *
    * This allows to not write a custom application.conf for testing and ensure play evolutions are disabled.
    */
  private def loadConfigWithoutEvolutions(env: Environment, container: PostgreSQLContainer): Configuration = {
    val map = Map(
      "db.default.username" -> container.username,
      "db.default.password" -> container.password,
      "db.default.url" -> container.jdbcUrl
    )

    Configuration.from(map).withFallback(Configuration.load(env))
  }

  def guiceApplicationBuilder(container: PostgreSQLContainer): GuiceApplicationBuilder =
    GuiceApplicationBuilder(loadConfiguration = env => loadConfigWithoutEvolutions(env, container))
      .in(Mode.Test)

  override def newAppForTest(testData: TestData): Application = {
    withContainers { postgres =>
      guiceApplicationBuilder(postgres).build()
    }
  }

  def withApiClient[A](runTest: AdminDataExplorerApiClient => A): A = {
    import sttp.client3.asynchttpclient.future.AsyncHttpClientFutureBackend

    implicit val sttpBackend: SttpBackend[Future, Any] = AsyncHttpClientFutureBackend()

    val config = AdminDataExplorerApiClient.Config(s"http://localhost:$port")
    val client = new AdminDataExplorerApiClient.DefaultImpl(config)
    runTest(client)
  }

  implicit class RichFutureExt[T](val future: Future[T]) {
    def expectError: String = future
      .map(_ => "Success when failure expected")
      .recover { case NonFatal(ex) =>
        ex.getMessage
      }
      .futureValue
  }
}
