package winter

import javax.servlet._
import http._

class Request(httpServletRequest:HttpServletRequest) {
  import _root_.java.{util => ju}
  import scala.collection.JavaConversions._
  import scala.collection.Map
  import scala.collection.mutable.LinkedHashMap

  def parameters: scala.collection.Map[String,String] = {
    val original = httpServletRequest.getParameterMap.asInstanceOf[ju.Map[String,Array[String]]]
    for ((key, values) <- original) yield (key -> values.mkString(""))
  }
}

trait Response {
  def writeTo(sink:Sink)
}

class Output(httpServletResponse:HttpServletResponse) {
  def stream = new Sink {
    val javaOutputStream = httpServletResponse.getOutputStream
    def writeBytes(bytes:Array[Byte]) = javaOutputStream.write(bytes)
  }
  def body(what:String) = httpServletResponse.getWriter.println(what)
}

trait Sink {
  def writeBytes(bytes:Array[Byte])
}