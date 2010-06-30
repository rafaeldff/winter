package winter.iogi

import _root_.winter.{Request, ParameterObjects}
import org.specs.Specification
import br.com.caelum.iogi.reflection.Target
import scala.collection.Map

class WinterIogiSpecification extends Specification {
  object IogiTest extends IogiScala

  "Winter Iogi integration" should {
    "instantiate a simple object" in {
      val parameters:Map[String,String] = Map("target.prop" -> "1337")
      val parameterObjects:ParameterObjects = IogiTest.createParameterObjects(requestForParameters(parameters))
      val newObject = parameterObjects[Sample]('target)
      newObject.prop must_== 1337
    }
    
    "instantiate a primitive" in {
      val parameters:Map[String,String] = Map("parameter" -> "1337")
      val parameterObjects:ParameterObjects = IogiTest.createParameterObjects(requestForParameters(parameters))
      val newObject = parameterObjects[Int]('parameter)
      newObject must_== 1337
    }

    "instantiate a String" in {
      val parameters:Map[String,String] = Map("parameter" -> "Thirteen hundred and thirty seven")
      val parameterObjects:ParameterObjects = IogiTest.createParameterObjects(requestForParameters(parameters))
      val newObject = parameterObjects[String]('parameter)
      newObject must_== "Thirteen hundred and thirty seven"
    }
  }

  def requestForParameters(theParameters:Map[String,String]) = new Request {
    def uri = ""
    def parameters = theParameters
  }
}

case class Sample(prop:Int)