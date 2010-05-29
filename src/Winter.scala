package winter
     
import _root_.hoops.Hoops

trait Response {
  def writeTo(sink:Sink)
}

trait Request {
  def parameters: scala.collection.Map[String,String] 
}

case class Parameter[T](value: T)

object Winter {
  case class TextResponse(text:String) extends Response {
    def writeTo(sink:Sink) = sink.writeBytes(text.getBytes)
  }

  case class HtmlResponse(element:Hoops.Element) extends Response {
    def writeTo(sink:Sink) = new TextResponse(element.toHtmlString).writeTo(sink)
  }

  def process(request:Request): Response = {
    import hoops.Hoops._

    implicit val req:Request = request

    implicit def parameterValues[T](implicit Request:Request): Parameter[T] = new Parameter[T](
      request.parameters.values.mkString(" ").asInstanceOf[T]
    )

    implicit def htmlSource(implicit toWhom:Parameter[String]):HtmlResponse =
      HtmlResponse (
        html(head(title("Hello World")),
        body(h1("Hello " + toWhom.value)))
      )

    implicitly[HtmlResponse]
  }
}

