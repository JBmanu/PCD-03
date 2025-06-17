import scala.language.postfixOps

ThisBuild / scalaVersion := "3.3.4"
ThisBuild / javacOptions ++= Seq("--release", "22")

enablePlugins(AssemblyPlugin)

lazy val root = (project in file("."))
  .aggregate(sudokuGrid, sudokuUI, sudokuLocal, sudokuMOM)
  .settings(
    name := "PCD-03",
    )

lazy val commonSettings = Seq(
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", _*) => MergeStrategy.discard
    case _                        => MergeStrategy.first
  },
  resolvers += "jitpack" at "https://jitpack.io",
  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-simple" % "2.0.17",
    "org.awaitility" % "awaitility" % "4.3.0" % Test,
    "org.junit.jupiter" % "junit-jupiter-api" % "5.13.1" % Test,
    )
  )

lazy val sudokuGrid = (project in file("sudokuGrid"))
  .settings(commonSettings *)
  .settings(
    name := "sudoku-grid",
    libraryDependencies ++= Seq(
      "de.sfuhrm" % "sudoku-client" % "5.0.3",
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
    libraryDependencies ++= Seq(
      "com.rabbitmq" % "amqp-client" % "5.25.0",
      "com.google.code.gson" % "gson" % "2.13.1",
      "com.github.rabbitmq" % "hop" % "5.3.0",
      )
    )

lazy val sudokuRMI = (project in file("sudoku-rmi"))
  .dependsOn(sudokuGrid % "compile->compile", sudokuMOM % "compile->compile")
  .settings(commonSettings *)
  .settings(
    name := "sudoku-rmi",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku-rmi.jar",
    libraryDependencies ++= Seq(

      )
    )