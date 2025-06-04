ThisBuild / scalaVersion := "3.3.4"

//libraryDependencies += "com.github.User" % "Repo" % "Tag"
//libraryDependencies += "com.github.USERNAME" %% "REPO" % "TAG"

lazy val root = (project in file(".")).aggregate(grid, sudoku)

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
    //    Compile / mainClass := Some("Main"),
    //    assembly / assemblyJarName := "grid.jar",
    libraryDependencies ++= Seq(
      "com.github.sfuhrm" % "sudoku" % "sudoku-parent-5.0.2",
      )
    )

lazy val sudoku = (project in file("sudoku"))
  .settings(commonSettings *)
  .settings(
    name := "sudoku",
    resolvers += "jitpack" at "https://jitpack.io",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku.jar",
    libraryDependencies ++= Seq(
      "com.github.sfuhrm" % "sudoku" % "sudoku-parent-5.0.2",
      )
    )