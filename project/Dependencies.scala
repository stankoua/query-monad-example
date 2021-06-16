import sbt._

object Dependencies {
  lazy val acolyte = "org.eu.acolyte" %% "jdbc-scala" % "1.0.54"

  lazy val acolytePlay = "org.eu.acolyte" %% "play-jdbc" % "1.0.54"

  lazy val anorm = "org.playframework.anorm" %% "anorm" % "2.6.5"

  lazy val cats = "org.typelevel" %% "cats-core" % "2.0.0"

  lazy val h2 = "com.h2database" % "h2" % "1.4.200"

  lazy val postgres = "org.postgresql" % "postgresql" % "42.2.10"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"

  lazy val specs2 = "org.specs2" %% "specs2-core" % "4.9.1"

  lazy val jbcrypt = "org.mindrot" % "jbcrypt" % "0.4"
}
