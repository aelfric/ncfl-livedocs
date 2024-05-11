ThisBuild / version := "0.1.0-SNAPSHOT"

libraryDependencies += "com.davegurnell" %% "spandoc" % "0.6.0"
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.5.4",
  "org.scalatest" %% "scalatest" % "3.2.18" % Test
)