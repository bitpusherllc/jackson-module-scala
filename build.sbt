// Basic facts
name := "jackson-module-scala"

organization := "com.fasterxml.jackson.module"

scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.9.1", "2.9.2", "2.9.3", "2.10.2")

scalacOptions ++= Seq("-deprecation", "-unchecked")

// Ensure jvm 1.6 for java
javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

// Try to future-proof scala jvm targets, in case some future
// scala version makes 1.7 a default
scalacOptions <+= (scalaBinaryVersion) map { binVer => binVer match {
  case "2.9.1" | "2.9.2" | "2.9.3" => "-target:jvm-1.5"
  case _ => "-target:jvm-1.6"
} }

// libraryDependencies <++= (scalaVersion) { (ver) => Seq("org.scala-lang" % "scalap" % ver) }

libraryDependencies <++= (version) { (v) =>
  val versionRange = "[2.3,"+v+"]"
  Seq(
    "com.fasterxml.jackson.core" % "jackson-core" % versionRange,
    "com.fasterxml.jackson.core" % "jackson-annotations" % versionRange,
    "com.fasterxml.jackson.core" % "jackson-databind" % versionRange,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % versionRange % "test",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-guava" % versionRange % "test",
    "com.fasterxml.jackson.module" % "jackson-module-jsonSchema" % versionRange % "test"
) }

libraryDependencies ++= Seq(
    "com.thoughtworks.paranamer" % "paranamer" % "2.6",
    "com.google.code.findbugs" % "jsr305" % "2.0.1",
    "com.google.guava" % "guava" % "15.0",
    // test dependencies
    "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
    "junit" % "junit" % "4.11" % "test",
    "com.novocode" % "junit-interface" % "0.10" % "test"
)

// resource filtering
seq(filterSettings: _*)

