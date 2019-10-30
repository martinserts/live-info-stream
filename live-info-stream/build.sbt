name := "live-info-stream"

version := "0.1"

scalaVersion := "2.13.1"

// Java betfair
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.0"
libraryDependencies += "com.github.joschi.jackson" % "jackson-datatype-threetenbp" % "2.10.0"

libraryDependencies += "io.swagger.core.v3" % "swagger-annotations" % "2.0.9"

libraryDependencies += "com.squareup.okhttp" % "okhttp" % "2.7.5"
libraryDependencies += "com.squareup.okhttp" % "logging-interceptor" % "2.7.5"

libraryDependencies += "io.gsonfire" % "gson-fire" % "1.8.3"

libraryDependencies += "com.sun.jersey" % "jersey-client" % "1.19.4"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
libraryDependencies += "org.slf4j" % "slf4j-ext" % "1.7.25"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"

// Scala
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test

libraryDependencies += "org.log4s" %% "log4s" % "1.8.2"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.0.0"
libraryDependencies += "org.typelevel" %% "cats-mtl-core" % "0.7.0"

val cirisVersion = "0.13.0-RC1"
libraryDependencies ++= Seq(
  "is.cir" %% "ciris-core",
  "is.cir" %% "ciris-cats",
  "is.cir" %% "ciris-cats-effect",
).map(_ % cirisVersion)

val http4sVersion = "0.21.0-M5"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

libraryDependencies += "co.fs2" %% "fs2-io" % "2.0.0"

val circeVersion = "0.12.2"
libraryDependencies += "io.circe" %% "circe-parser" % circeVersion
libraryDependencies += "io.circe" %% "circe-generic" % circeVersion
libraryDependencies += "io.circe" %% "circe-optics" % "0.12.0"

val monocleVersion = "2.0.0"
libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion
)