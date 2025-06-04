ThisBuild / scalaVersion := "3.3.4"

//libraryDependencies += "com.github.User" % "Repo" % "Tag"
//libraryDependencies += "com.github.USERNAME" %% "REPO" % "TAG"

lazy val sudoku = (project in file("sudoku"))
  .settings(
    name := "sudoku",
    resolvers += "jitpack" at "https://jitpack.io",
    Compile / mainClass := Some("Main"),
    assembly / assemblyJarName := "sudoku.jar",
    libraryDependencies ++= Seq(
      "com.github.sfuhrm" % "sudoku" % "sudoku-parent-5.0.2",
      "org.junit.jupiter" % "junit-jupiter-api" % "5.13.0" % Test,
      )
    )