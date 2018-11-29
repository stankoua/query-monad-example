import Dependencies._

name := "query-monad-code"

version := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.6"

// Common values
val commonSettings = Seq(
  organization := "com.zengularity",
  crossPaths := false,
  scalacOptions ++= Seq(
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
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused", // Warn if unused.
    "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
  ),
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
scalafmtOnCompile in ThisBuild := true
scalafmtOnCompile := true
scalafmtTestOnCompile in ThisBuild := true
scalafmtTestOnCompile := true
scalafmtConfig in ThisBuild := file("project/scalafmt.conf")

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
      Dependencies.postgres
    ),
    play.sbt.routes.RoutesKeys.routesImport := Seq.empty
  )
  .dependsOn(core, playSqlModule)

// Aggregate all projects

lazy val root: Project = project
  .in(file("."))
  .aggregate(core, playSqlModule, sampleAppExample, todoAppExample)
