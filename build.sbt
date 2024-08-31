ThisBuild / organization := "org.ncfl"
ThisBuild / version := "2.0.0"
ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .aggregate(schedule, filter)
  .settings(
    name := "ncfl-livedocs"
  )


lazy val schedule = (project in file("schedule"))
lazy val filter = (project in file("include-filter"))


