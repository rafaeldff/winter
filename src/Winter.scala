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

case class ParameterObjects[T](value: T)

trait Winter extends IogiScala {
  case class TextResponse(text:String) extends Response {
    def writeTo(sink:Sink) = sink.writeBytes(text.getBytes)
  }

  case class HtmlResponse(element: hoops.Hoops.Element) extends Response {
    def writeTo(sink:Sink) = new TextResponse(element.toHtmlString).writeTo(sink)
  }

  implicit def parameterValues[T, R <% Request](request:R): ParameterObjects[T] = new ParameterObjects[T] (
    request.parameters.values.mkString(" ").asInstanceOf[T]
  )

  def process(request:Request): Response                          
}


