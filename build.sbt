ThisBuild / scalaVersion := "3.3.4"

//libraryDependencies += "com.github.User" % "Repo" % "Tag"
//libraryDependencies += "com.github.USERNAME" %% "REPO" % "TAG"

lazy val root = (project in file("."))
  .aggregate(sudokuGrid, sudokuUI, sudokuLocal)
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

lazy val sudokuLocal = (project in file("sudoku-local"))
  .dependsOn(sudokuGrid % "compile->compile", sudokuUI % "compile->compile")
  .settings(commonSettings *)
  .settings(
    name := "sudoku-local",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku-local.jar",
    libraryDependencies ++= Seq(
    )
    )

lazy val sudokuMOM = (project in file("sudoku-mom"))
  .dependsOn(sudokuGrid % "compile->compile", sudokuUI % "compile->compile")
  .settings(commonSettings *)
  .settings(
    name := "sudoku-mom",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku-mom.jar",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "com.rabbitmq" % "amqp-client" % "5.25.0",
      "org.awaitility" % "awaitility" % "4.3.0",
      "com.github.rabbitmq" % "hop" % "5.3.0",
      "org.slf4j" % "slf4j-simple" % "2.0.17",
      )
    )