import sbt._
import Keys._

object Common {
  val appVersion = "1.0"

  val settings: Seq[Def.Setting[_]] = Seq(
    version := appVersion,
    //scalaVersion := "2.12.2",
    scalaVersion := "2.11.8",
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"), //, "-Xmx2G"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    resolvers += Opts.resolver.mavenLocalFile,
    resolvers += Resolver.bintrayRepo("cakesolutions", "maven"),
    resolvers ++= Seq(DefaultMavenRepository,
      Resolver.defaultLocal,
      Resolver.mavenLocal,
      "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",
      Classpaths.typesafeReleases,
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
      Classpaths.sbtPluginReleases,
      "Eclipse repositories" at "https://repo.eclipse.org/service/local/repositories/egit-releases/content/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")     
  )
}
