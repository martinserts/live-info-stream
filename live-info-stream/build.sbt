import Dependencies._

name := "live-info-stream"
version := "0.1"
scalaVersion := "2.13.4"

assemblyMergeStrategy in assembly := {
  case PathList("module-info.class", _ @ _*) => MergeStrategy.discard
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

libraryDependencies ++= Seq(
  Java.JavaxAnnotation,
  Java.JacksonCore,
  Java.JacksonThreetenbp,
  Java.Swagger,
  Java.OkHttp,
  Java.OkHttpLogging,
  Java.GsonFire,
  Java.Jersey,
  Java.Slf4j,
  Java.Slf4jExt,
  Java.Slf4jSimple,
  Scala.ScalaTest,
  Scala.Log4s,
  Scala.Cats,
  Scala.CatsEffect,
  Scala.CatsMtl,
  Scala.CatsLaws,
  Scala.Ciris,
  Scala.Http4s,
  Scala.Http4sCirce,
  Scala.Http4sServer,
  Scala.Http4sClient,
  Scala.Fs2,
  Scala.Circe,
  Scala.CirceGeneric,
  Scala.CirceOptics,
  Scala.Monocle,
  Scala.MonocleMacro,
  Scala.Upperbound
)
