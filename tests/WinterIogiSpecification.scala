package winter.iogi

import _root_.winter.ParameterObjects
import org.specs.Specification
import br.com.caelum.iogi.reflection.Target

class WinterIogiSpecification extends Specification {
  object IogiTest extends IogiScala

  "Winter Iogi integration" should {
    "instantiate a simple object" in {
      val parameters:Map[String,String] = Map("target.prop" -> "1337")
      val target = Target.create(classOf[Sample], "target")
      val newObject:ParameterObjects[Sample] = IogiTest.instantiate(target, parameters)
      newObject.value.prop must_== 1337
    }
  }
}

case class Sample(prop:Int)