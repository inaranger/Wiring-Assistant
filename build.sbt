ThisBuild / scalaVersion := "3.3.5"

name := "wiring-assistant"
version := "1.0"

lazy val root = (project in file("."))
  .settings(
    name := "Wiring-Assistant"
  )

Compile / mainClass := Some("Main")
