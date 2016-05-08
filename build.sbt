name := "mg_spark_ml"

organization := "it.mgaido"

version := "0.1"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.1" % "provided"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "1.6.1" % "provided"
libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test"
