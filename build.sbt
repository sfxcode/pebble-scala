import sbt.url
import ReleaseTransformations._

name := "pebble-scala"

organization := "com.sfxcode.templating"

crossScalaVersions := Seq("2.13.3", "2.12.12")

scalaVersion := crossScalaVersions.value.head

scalacOptions += "-deprecation"

parallelExecution in Test := false

lazy val docs = (project in file("docs"))
  .enablePlugins(ParadoxSitePlugin)
  .enablePlugins(ParadoxMaterialThemePlugin)
  .enablePlugins(GhpagesPlugin)
  .settings(
    name := "pebble scala docs",
    scalaVersion := "2.13.3",
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

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.5" % Test

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Test

libraryDependencies += "joda-time" % "joda-time" % "2.10.8" % Test

libraryDependencies += "io.pebbletemplates" % "pebble" % "3.1.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.3.1"

enablePlugins(BuildInfoPlugin)

buildInfoPackage := "com.sfxcode.templating.pebble"

buildInfoOptions += BuildInfoOption.BuildTime

buildInfoKeys ++= Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

// publish

releaseCrossBuild := true

bintrayReleaseOnPublish in ThisBuild := true

publishMavenStyle := true

homepage := Some(url("https://github.com/sfxcode/pebble-scala"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sfxcode/pebble-scala"),
    "scm:https://github.com/sfxcode/pebble-scala.git"
  )
)

developers := List(
  Developer(
    id = "sfxcode",
    name = "Tom Lamers",
    email = "tom@sfxcode.com",
    url = url("https://github.com/sfxcode")
  )
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies, // : ReleaseStep
  inquireVersions,           // : ReleaseStep
  runClean,                  // : ReleaseStep
  runTest,                   // : ReleaseStep
  setReleaseVersion,         // : ReleaseStep
  commitReleaseVersion,      // : ReleaseStep, performs the initial git checks
  tagRelease,                // : ReleaseStep
  publishArtifacts,          // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,            // : ReleaseStep
  commitNextVersion,         // : ReleaseStep
  pushChanges                // : ReleaseStep, also checks that an upstream branch is properly configured
)

scalafmtOnCompile := false

coverageMinimum := 70

coverageFailOnMinimum := true
