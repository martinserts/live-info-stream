import sbt._

object Dependencies {

  private object VersionOf {
    val JavaxAnnotation = "1.3.2"
    val Jackson         = "2.12.1"
    val Swagger         = "2.1.6"
    val OkHttp          = "2.7.5"
    val GsonFire        = "1.8.5"
    val Jersey          = "1.19.4"
    val Slf4j           = "1.7.30"

    val ScalaTest  = "3.2.3"
    val Log4s      = "1.9.0"
    val Cats       = "2.4.0"
    val CatsEffect = "2.3.0"
    val CatsMtl    = "0.7.1"
    val Ciris      = "1.2.1"
    val Http4s     = "0.21.18"
    val Fs2        = "2.5.0"
    val Circe      = "0.13.0"
    val Monocle    = "2.1.0"
    val Upperbound = "0.3.0"
  }

  object Java {
    val JavaxAnnotation   = "javax.annotation"           % "javax.annotation-api"        % VersionOf.JavaxAnnotation
    val JacksonCore       = "com.fasterxml.jackson.core" % "jackson-databind"            % VersionOf.Jackson
    val JacksonThreetenbp = "com.github.joschi.jackson"  % "jackson-datatype-threetenbp" % VersionOf.Jackson
    val Swagger           = "io.swagger.core.v3"         % "swagger-annotations"         % VersionOf.Swagger
    val OkHttp            = "com.squareup.okhttp"        % "okhttp"                      % VersionOf.OkHttp
    val OkHttpLogging     = "com.squareup.okhttp"        % "logging-interceptor"         % VersionOf.OkHttp
    val GsonFire          = "io.gsonfire"                % "gson-fire"                   % VersionOf.GsonFire
    val Jersey            = "com.sun.jersey"             % "jersey-client"               % VersionOf.Jersey
    val Slf4j             = "org.slf4j"                  % "slf4j-api"                   % VersionOf.Slf4j
    val Slf4jExt          = "org.slf4j"                  % "slf4j-ext"                   % VersionOf.Slf4j
    val Slf4jSimple       = "org.slf4j"                  % "slf4j-simple"                % VersionOf.Slf4j
  }

  object Scala {
    val ScalaTest    = "org.scalatest"              %% "scalatest"           % VersionOf.ScalaTest % Test
    val Log4s        = "org.log4s"                  %% "log4s"               % VersionOf.Log4s
    val Cats         = "org.typelevel"              %% "cats-core"           % VersionOf.Cats
    val CatsEffect   = "org.typelevel"              %% "cats-effect"         % VersionOf.CatsEffect
    val CatsMtl      = "org.typelevel"              %% "cats-mtl-core"       % VersionOf.CatsMtl
    val CatsLaws     = "org.typelevel"              %% "cats-effect-laws"    % VersionOf.CatsEffect
    val Ciris        = "is.cir"                     %% "ciris"               % VersionOf.Ciris
    val Http4s       = "org.http4s"                 %% "http4s-dsl"          % VersionOf.Http4s
    val Http4sCirce  = "org.http4s"                 %% "http4s-circe"        % VersionOf.Http4s
    val Http4sServer = "org.http4s"                 %% "http4s-blaze-server" % VersionOf.Http4s
    val Http4sClient = "org.http4s"                 %% "http4s-blaze-client" % VersionOf.Http4s
    val Fs2          = "co.fs2"                     %% "fs2-io"              % VersionOf.Fs2
    val Circe        = "io.circe"                   %% "circe-parser"        % VersionOf.Circe
    val CirceGeneric = "io.circe"                   %% "circe-generic"       % VersionOf.Circe
    val CirceOptics  = "io.circe"                   %% "circe-optics"        % VersionOf.Circe
    val Monocle      = "com.github.julien-truffaut" %% "monocle-core"        % VersionOf.Monocle
    val MonocleMacro = "com.github.julien-truffaut" %% "monocle-macro"       % VersionOf.Monocle
    val Upperbound   = "org.systemfw"               %% "upperbound"          % VersionOf.Upperbound
  }
}
