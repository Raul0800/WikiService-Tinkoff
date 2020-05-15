import sbt.Keys.libraryDependencies

val Http4sVersion  = "0.20.8"
val CirceVersion   = "0.11.1"
val Specs2Version  = "4.1.0"
val LogbackVersion = "1.2.3"
val FlywayVersion  = "6.4.1"
val DoobieVersion  = "0.9.0"
val CatsVersion    = "2.1.1"
val Pureconfig     = "0.12.2"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "WikiService",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.9",
    libraryDependencies ++= Seq(
      "org.typelevel"         %% "cats-core"              % CatsVersion,
      "org.http4s"            %% "http4s-blaze-server"    % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client"    % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"             % Http4sVersion,
      "ch.qos.logback"        % "logback-classic"         % LogbackVersion,
      "org.http4s"            %% "http4s-circe"           % Http4sVersion,
      "io.circe"              %% "circe-generic"          % CirceVersion,
      "io.circe"              %% "circe-literal"          % CirceVersion,
      "org.scalactic"         %% "scalactic"              % "3.0.8",
      "org.scalatest"         %% "scalatest"              % "3.1.1" % "test",
      "io.circe"              %% "circe-parser"           % CirceVersion,
      "org.tpolecat"          %% "doobie-core"            % DoobieVersion,
      "org.tpolecat"          %% "doobie-postgres"        % DoobieVersion,
      "org.tpolecat"          %% "doobie-scalatest"       % DoobieVersion % "test",
      "org.tpolecat"          %% "doobie-hikari"          % DoobieVersion,
      "com.github.pureconfig" %% "pureconfig"             % Pureconfig,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % Pureconfig,
      "org.flywaydb"          % "flyway-core"             % FlywayVersion,
      "io.chrisdavenport"     %% "log4cats-slf4j"         % "0.3.0",
      "ch.qos.logback"        % "logback-classic"         % "1.2.3",
      "org.scalamock"         %% "scalamock"              % "4.4.0" % Test,
      "org.scalatest"         %% "scalatest"              % "3.1.0" % Test
    )
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
  "-unchecked"
)
