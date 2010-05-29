package winter

trait Response {
  def writeTo(sink:Sink)
}

trait Request {
  def parameters: scala.collection.Map[String,String] 
}

case class Parameter[T](value: T)

trait Winter {
  case class TextResponse(text:String) extends Response {
    def writeTo(sink:Sink) = sink.writeBytes(text.getBytes)
  }

  case class HtmlResponse(element: hoops.Hoops.Element) extends Response {
    def writeTo(sink:Sink) = new TextResponse(element.toHtmlString).writeTo(sink)
  }

  implicit def parameterValues[T, R <% Request](request:R): Parameter[T] = new Parameter[T] (
    request.parameters.values.mkString(" ").asInstanceOf[T]
  )

  def process(request:Request): Response
}


