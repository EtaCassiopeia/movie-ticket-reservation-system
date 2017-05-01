import io.atomicbits.scraml.sbtplugin.ScramlSbtPlugin.autoImport._

scramlRamlApi in scraml in Compile :=  "com/veon/restclient/veon-api.raml"

//libraryDependencies ++= Seq(
//  "com.ning" % "async-http-client" % "1.9.36",
//  "io.atomicbits" %% "scraml-dsl-scala" % scramlVersion.value
//)
