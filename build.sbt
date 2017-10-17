name := "tag-search-server"

organization := "slrscrt.ru"

version := Versions.TagSearchServer

scalaVersion := Versions.Scala

mainClass := Some("ru.slrscrt.Application")

assemblyOutputPath in assembly := file(s"bin/lib/tag-search-server-${Versions.TagSearchServer}.jar")

scalacOptions := Seq(
  "-target:jvm-1.8",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-feature",
  "-deprecation",
  "-encoding", "utf8"
)

libraryDependencies ++= Dependencies.LibraryDependencies

test in assembly := {}