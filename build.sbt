import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ssync",
      version := "0.1",
      scalaVersion := "2.13.1"
    )),
    name := "ssync"
  )
libraryDependencies ++= Seq(
  "io.spray" %%  "spray-json" % "1.3.5",
  "org.scalactic" %% "scalactic" % "3.0.8",
  scalaTest % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test
)
parallelExecution in Test := false