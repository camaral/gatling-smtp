import sbt._

object Dependencies {
  private lazy val akkaVersion = "2.5.21"
  
  private lazy val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  private lazy val gatlingCore = "io.gatling" % "gatling-core" % "3.0.3"
  
  // TEST dependencies
  private lazy val akkaTest = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  private lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

  val rootDependencies = Seq(akka, gatlingCore, akkaTest, scalaTest)
}
