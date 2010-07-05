import sbt._

class WinterProject(info: ProjectInfo) extends ParentProject(info)
{
	
	lazy val core = project(".", "Winter core", new WinterCore(_))
	lazy val quotes = project("Quotes", "Quotes Sample", new Quotes(_), core)
	override def shouldCheckOutputDirectories = false


	class Quotes(info: ProjectInfo) extends DefaultProject(info) {
	  override def mainScalaSourcePath = "src"
		//override def fork = forkRun
	}

	class WinterCore(info: ProjectInfo) extends DefaultProject(info) {
		override def mainScalaSourcePath = "src"
		override def testScalaSourcePath = "tests"
		override def dependencyPath = "libs"
		override def testClasspath = super.testClasspath +++ ("testLibs" ** "*.jar")
	}

}
