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

// Used only by the server
lazy val baseServerSettings: Project => Project = {
  _.settings(
    scalacOptions ++= Seq(
      "-Werror",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8",
      "-Xsource:3",
      "-Wconf:src=src_managed/.*:silent",
      "-Xlint:missing-interpolator",
      "-Xlint:adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused"
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

lazy val spraWeb = (project in file("spra-web"))
  .dependsOn(spraApi.js)
  .configure(bundlerSettings, baseLibSettings)
  .configure(_.enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin))
  .settings(
    scalaVersion := "2.13.8",
    crossScalaVersions := Seq("2.13.8", "3.1.2"),
    name := "spra-web",
    Test / fork := false, // sjs needs this to run tests
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
