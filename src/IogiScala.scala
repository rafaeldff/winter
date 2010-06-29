package winter.iogi

import _root_.winter.ParameterObjects
import br.com.caelum.iogi.reflection.Target
import br.com.caelum.iogi.util.{DefaultLocaleProvider, NullDependencyProvider}
import br.com.caelum.iogi.parameters.{Parameters, Parameter}
import collection.JavaConversions

import java.util.{List => JList}
import collection.mutable.Buffer
import br.com.caelum.iogi.Iogi

trait IogiScala {
  def instantiate[T](target:Target[T], params:Map[String,String]): ParameterObjects[T] =  {
    import JavaConversions._

    val parameterList: Buffer[Parameter] = params.map{case (name, value) => new Parameter(name, value)}(scala.collection.breakOut)
    val parameters = new Parameters(parameterList)
    val newObject:T = new Iogi(new NullDependencyProvider, new DefaultLocaleProvider).instantiate(target, parameters)
    ParameterObjects(newObject)
  }
}


