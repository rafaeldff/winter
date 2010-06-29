package winter.iogi

import org.specs.Specification
import br.com.caelum.iogi.reflection.Target

class WinterIogiSpecification extends Specification {
  object IogiTest extends Iogi {

  };

  "Winter Iogi integration" should {
    "instantiate a simple object" in {
      val parameters:Map[String,String] = Map("target.value" -> "1337")
      val target = Target.create(classOf[Sample], "target")
      val newObject:Sample = IogiTest.instantiate(target, parameters)
      newObject.value must_== 1337
    }
  }
}

case class Sample(value:Int)