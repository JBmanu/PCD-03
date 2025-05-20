ThisBuild / scalaVersion := "3.3.4"

//libraryDependencies += "com.github.User" % "Repo" % "Tag"
//libraryDependencies += "com.github.USERNAME" %% "REPO" % "TAG"

lazy val sudoku = (project in file("sudoku"))
  .settings(
    name := "sudoku",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "com.github.sfuhrm" % "sudoku" % "sudoku-parent-5.0.2",
      // junit
      "org.junit.jupiter" % "junit-jupiter-api" % "5.10.3" % Test,
      )
    )