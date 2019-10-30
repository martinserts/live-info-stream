logLevel := Level.Warn

externalResolvers += Resolver.bintrayIvyRepo("evolutiongaming", "sbt-plugins")

addSbtPlugin("com.eed3si9n"  % "sbt-assembly" % "0.14.10")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.1.0")
addSbtPlugin("com.evolution" % "sbt-scalac-opts-plugin" % "0.0.9")
