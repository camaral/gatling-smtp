import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "camaral"
ThisBuild / organizationName := "camaral"

lazy val akkaVersion = "2.5.21"

lazy val root = (project in file("."))
  .settings(
    name := "gatling-smtp",
    libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
        "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
