package winter.iogi

import br.com.caelum.iogi.reflection.Target
import br.com.caelum.iogi.{Iogi => UnderlyingIogi}
import br.com.caelum.iogi.util.{DefaultLocaleProvider, NullDependencyProvider}
import br.com.caelum.iogi.parameters.{Parameters, Parameter}
import collection.JavaConversions

import java.util.{List => JList}
import collection.mutable.Buffer

trait Iogi {
  def instantiate[T](target:Target[T], params:Map[String,String]) =  {
    import JavaConversions._

    val parameterList: Buffer[Parameter] = params.map{case (name, value) => new Parameter(name, value)}(scala.collection.breakOut)
    val parameters = new Parameters(parameterList)
    new UnderlyingIogi(new NullDependencyProvider, new DefaultLocaleProvider).instantiate(target, parameters)
  }
}


