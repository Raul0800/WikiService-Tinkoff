val Http4sVersion  = "0.20.8"
val CirceVersion   = "0.11.1"
val Specs2Version  = "4.1.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "WikiService",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.9",
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"          % Http4sVersion,
      "ch.qos.logback"        % "logback-classic"      % LogbackVersion,
      "org.http4s"            %% "http4s-circe"        % Http4sVersion,
      "io.circe"              %% "circe-generic"       % CirceVersion,
      "io.circe"              %% "circe-literal"       % CirceVersion,
      "org.scalactic"         %% "scalactic"           % "3.0.8",
      "org.scalatest"         %% "scalatest"           % "3.0.8" % "test",
      "io.circe"              %% "circe-parser"        % CirceVersion,
      "org.tpolecat"          %% "doobie-core"         % "0.8.8",
      "org.tpolecat"          %% "doobie-postgres"     % "0.8.8",
      "org.tpolecat"          %% "doobie-scalatest"    % "0.8.8" % "test",
      "com.github.pureconfig" %% "pureconfig"          % "0.12.2"
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
