import sbt._

class WinterProject(info: ProjectInfo) extends ParentProject(info)
{
	
	lazy val core = project(".", "Winter core", new WinterCore(_))
	lazy val quotes = project("Quotes", "Quotes Sample", new Quotes(_), core)
	override def shouldCheckOutputDirectories = false

	val dependsOnHoops = "rafaelferreira.net" %% "hoops" % "0.5-SNAPSHOT"

	class Quotes(info: ProjectInfo) extends DefaultWebProject(info) {
	  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test"
    val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"
    val derby = "org.apache.derby" % "derby" % "10.2.2.0" % "runtime"
    val junit = "junit" % "junit" % "3.8.1" % "test"

	  override def mainScalaSourcePath = "src"
	  override def mainResourcesPath = "src"
	  override def webappPath = "webapp"
	}

	class WinterCore(info: ProjectInfo) extends DefaultProject(info) {
		val dependsOnHoops = "rafaelferreira.net" %% "hoops" % "0.5-SNAPSHOT"
		override def mainScalaSourcePath = "src"
		override def testScalaSourcePath = "tests"
		override def dependencyPath = "libs"
		override def testClasspath = super.testClasspath +++ ("testLibs" ** "*.jar")

	}

}
