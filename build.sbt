ThisBuild / scalaVersion := "3.3.4"

//libraryDependencies += "com.github.User" % "Repo" % "Tag"
//libraryDependencies += "com.github.USERNAME" %% "REPO" % "TAG"

lazy val root = (project in file("."))
  .aggregate(sudokuGrid, sudoku)
  .settings(
    name := "PCD-03",
  )

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.junit.jupiter" % "junit-jupiter-api" % "5.13.0" % Test,
    )
  )

lazy val sudokuGrid = (project in file("sudokuGrid"))
  .settings(commonSettings *)
  .settings(
    name := "sudoku-grid",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "com.github.sfuhrm" % "sudoku" % "sudoku-parent-5.0.2",
      )
    )

lazy val sudokuUI = (project in file("sudoku-ui"))
  .dependsOn(sudokuGrid % "compile->compile")
  .settings(commonSettings *)
  .settings(
    name := "sudoku-ui",
    libraryDependencies ++= Seq(
      )
    )

lazy val sudoku = (project in file("sudoku-local"))
  .dependsOn(sudokuGrid % "compile->compile", sudokuUI % "compile->compile")
  .settings(commonSettings *)
  .settings(
    name := "sudoku-local",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku.jar",
    libraryDependencies ++= Seq(
      )
    )