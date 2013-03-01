import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "newsseeder"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "gov.sandia.foundry" % "cognitive-foundry" % "3.3.2",
    "de.l3s.boilerpipe" % "boilerpipe" % "1.2.0",
    "org.bluestemsoftware.open.maven.tparty" % "xerces-impl" % "2.9.0",
    "org.webjars" % "webjars-play" % "2.1.0",
    "org.webjars" % "bootstrap" % "2.3.0",
    "edu.stanford.nlp" % "stanford-corenlp" % "1.3.4",
   	"net.sourceforge.nekohtml" % "nekohtml" % "1.9.17",
   	"rome" % "rome" % "1.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "boilerpipe" at "http://boilerpipe.googlecode.com/svn/repo/"  
  )

}
