import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

ThisBuild / versionScheme := Some("early-semver")
// For all Sonatype accounts created on or after February 2021
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

inThisBuild(
  List(
    organization := "net.wiringbits",
    name := "scala-postgres-react-admin",
    homepage := Some(url("https://github.com/wiringbits/scala-postgres-react-admin")),
    licenses := List("MIT" -> url("https://www.opensource.org/licenses/mit-license.html")),
    developers := List(
      Developer(
        "AlexITC",
        "Alexis Hernandez",
        "alexis22229@gmail.com",
        url("https://wiringbits.net")
      )
    )
  )
)

resolvers += Resolver.sonatypeRepo("releases")

val playJson = "2.10.0-RC5"
val sttp = "3.5.0"

val consoleDisabledOptions = Seq("-Xfatal-warnings", "-Ywarn-unused", "-Ywarn-unused-import")

lazy val build = TaskKey[File]("build")

// Used only by the server
lazy val baseServerSettings: Project => Project = {
  _.settings(
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature"
    ),
    Compile / doc / scalacOptions ++= Seq("-no-link-warnings"),
    // Some options are very noisy when using the console and prevent us using it smoothly, let's disable them
    Compile / console / scalacOptions ~= (_.filterNot(consoleDisabledOptions.contains))
  )
}

// Used only by play-based projects
lazy val playSettings: Project => Project = {
  _.enablePlugins(PlayScala)
    .disablePlugins(PlayLayoutPlugin)
    .settings(
      // docs are huge and unnecessary
      Compile / doc / sources := Nil,
      Compile / doc / scalacOptions ++= Seq(
        "-no-link-warnings"
      ),
      // remove play noisy warnings
      play.sbt.routes.RoutesKeys.routesImport := Seq.empty,
      libraryDependencies ++= Seq(
        evolutions,
        "com.typesafe.play" %% "play-jdbc" % "2.8.13",
        "com.google.inject" % "guice" % "5.1.0"
      ),
      // test
      libraryDependencies ++= Seq(
        "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
        "org.mockito" %% "mockito-scala" % "1.17.5" % Test,
        "org.mockito" %% "mockito-scala-scalatest" % "1.17.5" % Test
      )
    )
}

// Used only by the lib projects
lazy val baseLibSettings: Project => Project = _.settings(
  scalacOptions ++= {
    Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-language:implicitConversions"
      // disabled during the migration
      // "-Xfatal-warnings"
    ) ++
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) =>
          Seq(
            "-unchecked",
            "-source:3.0-migration"
          )
        case _ =>
          Seq(
            "-deprecation",
            "-Xfatal-warnings",
            "-Wunused:imports,privates,locals",
            "-Wvalue-discard"
          )
      })
  },
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.2.12" % Test
  )
)

// The common stuff for the server/client modules
lazy val spraCommon = (crossProject(JSPlatform, JVMPlatform) in file("spra-common"))
  .configure(baseLibSettings)
  .settings(
    scalaVersion := "2.13.8",
    crossScalaVersions := Seq("2.13.8", "3.1.2"),
    name := "spra-common"
  )
  .jsConfigure(_.enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin))
  .jvmSettings(
    libraryDependencies ++= Seq(
      // TODO: This shouldn't depend in play-json but I'm leaving it for simplicity
      "com.typesafe.play" %% "play-json" % playJson
    )
  )
  .jsSettings(
    stUseScalaJsDom := true,
    Test / fork := false, // sjs needs this to run tests
    Compile / stMinimize := Selection.All,
    libraryDependencies ++= Seq(
      // TODO: This shouldn't depend in play-json but I'm leaving it for simplicity
      "com.typesafe.play" %%% "play-json" % playJson
    )
  )

// Just the API side for the SPRA modules
lazy val spraApi = (crossProject(JSPlatform, JVMPlatform) in file("spra-api"))
  .configure(baseLibSettings)
  .dependsOn(spraCommon)
  .settings(
    scalaVersion := "2.13.8",
    crossScalaVersions := Seq("2.13.8", "3.1.2"),
    name := "spra-api"
  )
  .jsConfigure(_.enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin))
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % playJson,
      "com.softwaremill.sttp.client3" %% "core" % sttp
    )
  )
  .jsSettings(
    stUseScalaJsDom := true,
    Test / fork := false, // sjs needs this to run tests
    Compile / stMinimize := Selection.All,
    libraryDependencies ++= Seq(
      "com.typesafe.play" %%% "play-json" % playJson,
      "com.softwaremill.sttp.client3" %%% "core" % sttp
    )
  )

/** Includes the specific stuff to run the SPRA server side (play-specific)
  */
lazy val spraPlayServer = (project in file("spra-play-server"))
  .dependsOn(spraApi.jvm, spraCommon.jvm)
  .configure(baseServerSettings, playSettings)
  .settings(
    scalaVersion := "2.13.8",
    crossScalaVersions := Seq("2.13.8"),
    name := "spra-play-server",
    fork := true,
    Test / fork := true, // allows for graceful shutdown of containers once the tests have finished running
    libraryDependencies ++= Seq(
      guice,
      "org.playframework.anorm" %% "anorm" % "2.6.10",
      "com.typesafe.play" %% "play" % "2.8.13",
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "org.postgresql" % "postgresql" % "42.3.6",
      "com.github.jwt-scala" %% "jwt-core" % "9.0.5",
      "de.svenkubiak" % "jBCrypt" % "0.4.3",
      "commons-validator" % "commons-validator" % "1.7",
      "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.7" % "test",
      "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.40.7" % "test",
      "com.softwaremill.sttp.client3" %% "core" % sttp % "test",
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-future" % sttp % "test"
    )
  )

lazy val bundlerSettings: Project => Project =
  _.settings(
    Compile / fastOptJS / webpackExtraArgs += "--mode=development",
    Compile / fullOptJS / webpackExtraArgs += "--mode=production",
    Compile / fastOptJS / webpackDevServerExtraArgs += "--mode=development",
    Compile / fullOptJS / webpackDevServerExtraArgs += "--mode=production"
  )

lazy val spraWebBuildInfoSettings: Project => Project = _.enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoKeys ++= {
      val apiUrl = sys.env.get("API_URL")
      val values = Seq(
        "apiUrl" -> apiUrl
      )
      // Logging these values is useful to make sure that the necessary settings
      // are being overriden when packaging the app.
      sLog.value.info(s"BuildInfo settings:\n${values.mkString("\n")}")
      values.map(t => BuildInfoKey(t._1, t._2))
    },
    buildInfoPackage := "net.wiringbits",
    buildInfoUsePackageAsPath := true
  )

lazy val browserProject: Project => Project =
  _.settings(
    build := {
      val artifacts = (Compile / fullOptJS / webpack).value
      val artifactFolder = (Compile / fullOptJS / crossTarget).value
      val jsFolder = baseDirectory.value / "src" / "main" / "js"
      val distFolder = baseDirectory.value / "build"

      distFolder.mkdirs()
      artifacts.foreach { artifact =>
        val target = artifact.data.relativeTo(artifactFolder) match {
          case None => distFolder / artifact.data.name
          case Some(relFile) => distFolder / relFile.toString
        }

        Files.copy(artifact.data.toPath, target.toPath, REPLACE_EXISTING)
      }

      // copy public resources
      Files
        .walk(jsFolder.toPath)
        .filter(x => !Files.isDirectory(x))
        .forEach(source => {
          source.toFile.relativeTo(jsFolder).foreach { relativeSource =>
            val dest = distFolder / relativeSource.toString
            dest.getParentFile.mkdirs()
            Files.copy(source, dest.toPath, REPLACE_EXISTING)
          }
        })

      // link the proper js bundle
      val indexFrom = baseDirectory.value / "src/main/js/index.html"
      val indexTo = distFolder / "index.html"

      val indexPatchedContent = {
        import collection.JavaConverters._
        Files
          .readAllLines(indexFrom.toPath, IO.utf8)
          .asScala
          .map(_.replaceAllLiterally("-fastopt", "-opt"))
          .mkString("\n")
      }

      Files.write(indexTo.toPath, indexPatchedContent.getBytes(IO.utf8))
      distFolder
    }
  )

lazy val spraWeb = (project in file("spra-web"))
  .dependsOn(spraApi.js, spraPlayServer)
  .configure(bundlerSettings, baseLibSettings, browserProject, spraWebBuildInfoSettings)
  .configure(_.enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin))
  .settings(
    scalaVersion := "2.13.8",
    crossScalaVersions := Seq("2.13.8", "3.1.2"),
    name := "spra-web",
    Test / fork := false, // sjs needs this to run tests
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig := scalaJSLinkerConfig.value.withSourceMap(false),
    webpackDevServerPort := 8081,
    webpackBundlingMode := BundlingMode.LibraryOnly(),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scala-js-macrotask-executor" % "1.0.0",
      "me.shadaj" %%% "slinky-core" % "0.7.3",
      "me.shadaj" %%% "slinky-web" % "0.7.3"
    ),
    Compile / npmDependencies ++= Seq(
      "react" -> "17.0.0",
      "react-dom" -> "17.0.0",
      "react-scripts" -> "5.0.0",
      "react-admin" -> "4.1.0",
      "ra-ui-materialui" -> "4.1.0",
      "ra-data-simple-rest" -> "4.1.0",
      "ra-i18n-polyglot" -> "4.1.0",
      "ra-language-english" -> "4.1.0",
      "ra-core" -> "4.1.0",
      "@mui/material" -> "5.8.1",
      "@emotion/styled" -> "11.8.1"
    )
  )

lazy val root = (project in file("."))
  .aggregate(
    spraCommon.jvm,
    spraCommon.js,
    spraApi.jvm,
    spraApi.js,
    spraPlayServer,
    spraWeb
  )
  .settings(
    name := "scala-postgres-react-admin",
    publish := {},
    publishLocal := {},
    publish / skip := true
  )

addCommandAlias("spra-admin", ";spraWeb/fastOptJS::startWebpackDevServer;~spraWeb/fastOptJS")
