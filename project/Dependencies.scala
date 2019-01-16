import sbt._

object Dependencies {
  lazy val acolyte = "org.eu.acolyte" %% "jdbc-scala" % "1.0.51"

  lazy val acolytePlay = "org.eu.acolyte" %% "play-jdbc" % "1.0.51"

  lazy val anorm = "org.playframework.anorm" %% "anorm" % "2.6.2"

  lazy val cats = "org.typelevel" %% "cats-core" % "1.4.0"
  
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "1.0.0"

  lazy val h2 = "com.h2database" % "h2" % "1.4.197"

  lazy val postgres = "org.postgresql" % "postgresql" % "42.2.5"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

  lazy val specs2 = "org.specs2" %% "specs2-core" % "4.3.5"

  lazy val jbcrypt = "org.mindrot" % "jbcrypt" % "0.4"
}
