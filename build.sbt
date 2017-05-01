name := "movie-ticket-reservation"

organization := "com.veon"

version := "1.0"

//scalaVersion := "2.12.2"
scalaVersion := "2.11.8"

lazy val common = project.
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.commonDependencies)

lazy val `movie-service` = (project in file("movie-service")).
  dependsOn(common).
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.serviceDependencies).
  settings(
    Seq(
      mainClass in Compile := Some("com.veon.movie.restapi.Main"),
      dockerBaseImage := "java:8",
      //dockerBaseImage := "openjdk:8-jdk-alpine",
      dockerRepository := Some("veon.com/zainalpour"),
      packageName in Docker := name.value,
      dockerUpdateLatest := true,
      version in Docker := "latest",
      defaultLinuxInstallLocation in Docker := s"/opt/${name.value}",
      dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf")
    )
  ).enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val `reservation-service` = (project in file("reservation-service")).
  dependsOn(common).
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.serviceDependencies).
  settings(
    Seq(
      mainClass in Compile := Some("com.veon.reservation.restapi.Main"),
      //dockerBaseImage := "openjdk:8-jdk-alpine",
      dockerRepository := Some("veon.com/zainalpour"),
      packageName in Docker := name.value,
      dockerUpdateLatest := true,
      version in Docker := "latest",
      defaultLinuxInstallLocation in Docker := s"/opt/${name.value}",
      dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf")
    )
  ).enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val `aggregation-service` = (project in file("aggregation-service")).
  dependsOn(common).
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.serviceDependencies).
  settings(
    Seq(
      mainClass in Compile := Some("com.veon.aggregation.restapi.Main"),
      //dockerBaseImage := "openjdk:8-jdk-alpine",
      dockerRepository := Some("veon.com/zainalpour"),
      packageName in Docker := name.value,
      dockerUpdateLatest := true,
      version in Docker := "latest",
      defaultLinuxInstallLocation in Docker := s"/opt/${name.value}",
      dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf")
    )
  ).enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val root = (project in file(".")).
  aggregate(common, `movie-service`, `reservation-service`, `aggregation-service`)
