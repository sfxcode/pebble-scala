import sbt.url
import ReleaseTransformations._

name := "pebble-scala"

crossScalaVersions := Seq("2.13.6", "3.0.0", "2.12.11")

scalaVersion := crossScalaVersions.value.head

scalacOptions += "-deprecation"

Test / parallelExecution := false

lazy val docs = (project in file("docs"))
  .enablePlugins(ParadoxSitePlugin)
  .enablePlugins(ParadoxMaterialThemePlugin)
  .enablePlugins(GhpagesPlugin)
  .settings(
    name := "pebble scala docs",
    scalaVersion := "2.13.6",
    publish / skip := true,
    ghpagesNoJekyll := true,
    git.remoteRepo := "git@github.com:sfxcode/pebble-scala.git",
    Compile / paradoxMaterialTheme ~= {
      _.withRepository(uri("https://github.com/sfxcode/pebble-scala"))
    },
    (Compile / paradoxMarkdownToHtml / excludeFilter) := (Compile / paradoxMarkdownToHtml / excludeFilter).value ||
    ParadoxPlugin.InDirectoryFilter((Compile / paradox / sourceDirectory).value / "includes")
  )

buildInfoOptions += BuildInfoOption.BuildTime

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

// Test dependencies

libraryDependencies += "org.scalameta" %% "munit" % "0.7.26" % Test

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Test

libraryDependencies += "joda-time" % "joda-time" % "2.10.10" % Test

// Runtime dependencies

libraryDependencies += "io.pebbletemplates" % "pebble" % "3.1.5"

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.4.4"

// Plugins

enablePlugins(BuildInfoPlugin)

buildInfoPackage := "com.sfxcode.templating.pebble"

buildInfoOptions += BuildInfoOption.BuildTime

buildInfoKeys ++= Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

scalafmtOnCompile := false

coverageMinimumStmtTotal := 70

coverageFailOnMinimum := true
