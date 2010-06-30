package winter

import _root_.winter.iogi.IogiScala

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

  case class HtmlResponse(element: hoops.Hoops.Element) extends Response {
    def writeTo(sink:Sink) = new TextResponse(element.toHtmlString).writeTo(sink)
  }

  def process(request:Request): Response                          
}


