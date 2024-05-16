import ReleaseTransformations._

ThisBuild / intellijPluginName := "intellij-dhall"
ThisBuild / intellijBuild := "241.17011.2"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion, // performs the initial git checks
  tagRelease,
  pushChanges,
  setNextVersion,
  commitNextVersion,
)

releaseTagComment        := s"[auto-package] Release ${(ThisBuild / version).value}"
releaseCommitMessage     := s"[auto-package] Set release version to ${(ThisBuild / version).value}"
releaseNextCommitMessage := s"[auto-package] Set next development version to ${(ThisBuild / version).value}"

lazy val dhall = project
  .in(file("."))
  .enablePlugins(SbtIdeaPlugin)
  .settings(
    scalaVersion := "2.13.14",
    Compile / scalaSource := baseDirectory.value / "src" / "main" / "scala",
    Test / scalaSource := baseDirectory.value / "src" / "test" / "scala",
    ideBasePackages := Seq("org.intellij.plugins.dhall"),
    Compile / unmanagedSourceDirectories += baseDirectory.value / "gen",
    Compile / resourceDirectory := baseDirectory.value / "resources",
    packageMethod := PackagingMethod.Standalone(targetPath = s"lib/${name.value}-${(ThisBuild / version).value}.jar"),

    patchPluginXml := pluginXmlOptions { xml =>
      xml.version = (ThisBuild / version).value
      xml.sinceBuild = (ThisBuild / intellijBuild).value
    },
    //intellijRuntimePlugins += "idea.plugin.psiviewer".toPlugin, // Fails to download.
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % Test
    )
  )

//lazy val runner = createRunnerProject(dhall)
