package winter.iogi

import _root_.winter.{Request, ParameterObjects}
import br.com.caelum.iogi.reflection.Target
import br.com.caelum.iogi.util.{DefaultLocaleProvider, NullDependencyProvider}
import br.com.caelum.iogi.parameters.{Parameters, Parameter}

import java.util.{List => JList}
import collection.{JavaConversions, Map}
import collection.mutable.Buffer
import br.com.caelum.iogi.Iogi

trait IogiScala {
  def instantiate[T](target:Target[T], params:Map[String,String]): T =  {
    import JavaConversions._

    val parameterList: Buffer[Parameter] = params.map{case (name, value) => new Parameter(name, value)}(scala.collection.breakOut)
    val parameters = new Parameters(parameterList)
    val newObject:T = new Iogi(new NullDependencyProvider, new DefaultLocaleProvider).instantiate(target, parameters)
    newObject
  }

  implicit def createParameterObjects[R <% Request](request:R):ParameterObjects = new ParameterObjects {
    def apply[T: Manifest](targetName: Symbol):T = {
      val clazz = implicitly[Manifest[T]].erasure
      val target = new Target[T](clazz, targetName.name)
      instantiate(target, request.parameters)
    }
  }
}


