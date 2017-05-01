import sbt._

object Dependencies {

  val akkaVersion = "2.5.0"

  val akkaHttpVersion = "10.0.5"
  val scalaTestVersion = "3.0.1"
  val slickVersion = "3.2.0"
  val circeVersion = "0.7.1"
  val scramlVersion = "0.6.1"
  val consulClientVersion = "0.14.0"

  val commonDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

    "org.slf4j" % "slf4j-nop" % "1.6.4",

    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",

    "com.zaxxer" % "HikariCP" % "2.4.5",
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "1.15" % "test",

    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",

    "com.ning" % "async-http-client" % "1.9.36",
    "io.atomicbits" % "scraml-dsl-scala_2.11" % scramlVersion,

    "com.orbitz.consul" % "consul-client" % consulClientVersion
  )

  val serviceDependencies: Seq[ModuleID] = commonDependencies ++ Seq(

    "de.heikoseeberger" %% "akka-http-circe" % "1.15.0",

    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,

    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
  )

}
