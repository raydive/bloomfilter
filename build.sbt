ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "BloomFilter",
    libraryDependencies += "com.google.guava" % "guava" % "33.4.0-jre",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test,
  )
