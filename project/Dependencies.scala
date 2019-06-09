import sbt._

object Dependencies {
  private lazy val akkaVersion = "2.5.21"

  private lazy val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  private lazy val gatlingCore = "io.gatling" % "gatling-core" % "3.0.3"
  private lazy val gatlingRecorder = "io.gatling" % "gatling-recorder" % "3.0.3"
  private lazy val courier = "com.github.daddykotex" % "courier_2.12" % "1.0.0"
  private lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  private lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

  // TEST dependencies
  private lazy val akkaTest = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  private lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  private lazy val gatlingHighCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.3" % "test"
  private lazy val gatlingTest = "io.gatling" % "gatling-test-framework" % "3.0.3" % "test"

  val rootDependencies = Seq(akka, gatlingCore, gatlingRecorder, courier, scalaLogging,
    logback, akkaTest, scalaTest, gatlingHighCharts, gatlingTest)
}
