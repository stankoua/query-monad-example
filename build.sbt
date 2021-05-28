name := "query-monad-code"

version := "1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.14"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.14")

def scalacOptionsVersion(scalaVersion: String) = {
  val defaultOptions = Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfatal-warnings", // Fail the compilation if there are any warnings.
    "-Xlint",
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification", // Enable partial unification in type constructor inference
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused", // Warn if unused.
    "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
  )
  val v211Options = Seq(
    "-Xsource:2.12" // See https://github.com/scala/scala/releases/tag/v2.11.11
  )
  val v212Options = Seq(
    "-Ywarn-extra-implicit" // Warn when more than one implicit parameter section is defined.
  )

  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2L, 11L)) => defaultOptions ++ v211Options
    case _               => defaultOptions ++ v212Options
  }
}

// Common values
def commonSettings = Seq(
  organization := "com.zengularity",
  crossPaths := false,
  scalacOptions ++= scalacOptionsVersion(scalaVersion.value),
  scalacOptions in (Compile, console) ~= (_.filterNot(
    Set(
      "-Ywarn-unused:imports",
      "-Xfatal-warnings"
    )
  )),
  scalacOptions in (Test, compile) ~= (_.filterNot(
    Set(
      "-Ywarn-unused:imports",
      "-Xfatal-warnings",
      "-Yrangepos"
    )
  )),
  resolvers ++= Seq[Resolver](
    Resolver.sonatypeRepo("releases")
  )
)

// Scalafmt
ThisBuild / scalafmtOnCompile := true

// Wartremover
wartremoverErrors ++= Warts.unsafe

//
// Projects definitions
//

// Core + Modules

lazy val core = (project in file("core"))
  .settings(
    commonSettings ++ Seq(
      name := "query-core",
      libraryDependencies ++= Seq(
        Dependencies.acolyte % Test,
        Dependencies.anorm   % Test,
        Dependencies.cats,
        Dependencies.specs2 % Test
      )
    )
  )

lazy val playSqlModule = (project in file("modules/play-sql"))
  .settings(commonSettings)
  .settings(
    name := "query-play-sql",
    libraryDependencies ++= Seq(
      jdbc,
      evolutions               % Test,
      logback                  % Test,
      Dependencies.acolyte     % Test,
      Dependencies.acolytePlay % Test,
      Dependencies.anorm       % Test,
      Dependencies.h2          % Test,
      Dependencies.scalaLogging,
      Dependencies.specs2 % Test
    )
  )
  .dependsOn(core % "test->test;compile->compile")

// Examples

lazy val sampleAppExample = (project in file("examples/sample-app"))
  .enablePlugins(PlayScala)
  .settings(
    commonSettings ++ Seq(
      name := "sample-app-example",
      libraryDependencies ++= Seq(
        Dependencies.anorm,
        Dependencies.h2
      )
    )
  )
  .dependsOn(core, playSqlModule)

lazy val todoAppExample = (project in file("examples/todo-app"))
  .enablePlugins(PlayScala)
  .settings(commonSettings)
  .settings(
    name := "todo-app-example",
    libraryDependencies ++= Seq(
      evolutions,
      Dependencies.anorm,
      Dependencies.postgres,
      Dependencies.jbcrypt
    ),
    play.sbt.routes.RoutesKeys.routesImport := Seq(
      "java.util.UUID"
    )
  )
  .dependsOn(core, playSqlModule)

// Aggregate all projects

lazy val root: Project = project
  .in(file("."))
  .aggregate(core, playSqlModule, sampleAppExample, todoAppExample)
