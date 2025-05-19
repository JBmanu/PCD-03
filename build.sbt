

ThisBuild / scalaVersion := "3.3.4"

lazy val sudoku = (project in file("sudoku"))
  .settings(
    name := "sudoku",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test
    )