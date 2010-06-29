import sbt._

class WinterProject(info: ProjectInfo) extends DefaultProject(info)
{
  override def mainScalaSourcePath = "src"
	override def testScalaSourcePath = "tests"
	override def dependencyPath = "libs"
	override def testClasspath = super.testClasspath +++ ("testLibs" ** "*.jar")
}
