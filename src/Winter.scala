package winter

import winter.iogi.IogiScala
import hoops._

trait Response {
  def writeTo(sink:Sink)
}

trait Request {
  def parameters: scala.collection.Map[String,String]
  def uri: String
}

object Path {
  def unapplySeq(request:Request): Option[Seq[String]] =
    Some(request.uri.split("/").drop(1))
}

trait ParameterObjects {
  def apply[T: Manifest](targetName:Symbol):T
}

trait Winter extends IogiScala {
  case class TextResponse(text:String) extends Response {
    def writeTo(sink:Sink) = sink.writeBytes(text.getBytes)
  }

  case class HtmlResponse(element: Base#Element) extends Response {
    val doctype = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">"""

    def writeTo(sink:Sink) = new TextResponse(doctype + element.toHtmlString).writeTo(sink)
  }

  def process(request:Request): Response                          
}


