import sbt._

object Dependencies {
  private lazy val akkaVersion = "2.5.21"
  
  private lazy val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  private lazy val gatlingCore = "io.gatling" % "gatling-core" % "3.0.3"
  private lazy val courier = "com.github.daddykotex" % "courier_2.12" % "1.0.0"
  
  // TEST dependencies
  private lazy val akkaTest = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  private lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

  val rootDependencies = Seq(akka, gatlingCore, courier, akkaTest, scalaTest)
}
