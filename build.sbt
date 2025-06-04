ThisBuild / scalaVersion := "3.3.4"

//libraryDependencies += "com.github.User" % "Repo" % "Tag"
//libraryDependencies += "com.github.USERNAME" %% "REPO" % "TAG"

lazy val root = (project in file("."))
  .aggregate(grid, sudoku)
  .settings(
    name := "PCD-03",
  )

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.junit.jupiter" % "junit-jupiter-api" % "5.13.0" % Test,
    )
  )

lazy val grid = (project in file("grid"))
  .settings(commonSettings *)
  .settings(
    name := "grid",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "com.github.sfuhrm" % "sudoku" % "sudoku-parent-5.0.2",
      )
    )

lazy val sudoku = (project in file("sudoku"))
  .dependsOn(grid % "compile->compile")
  .settings(commonSettings *)
  .settings(
    name := "sudoku",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku.jar",
    libraryDependencies ++= Seq(
      )
    )