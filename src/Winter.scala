package winter
     
import _root_.hoops.Hoops

trait Response {
  def writeTo(sink:Sink)
}

trait Request {
  def parameters: scala.collection.Map[String,String] 
}

case class Parameter(value: String)

object Winter {
  case class TextResponse(text:String) extends Response {
    def writeTo(sink:Sink) = sink.writeBytes(text.getBytes)
  }

  case class HtmlResponse(element:Hoops.Element) extends Response {
    def writeTo(sink:Sink) = new TextResponse(element.toHtmlString).writeTo(sink)
  }

  def process(request:Request): Response = {
    import hoops.Hoops._

    implicit def parameterValues[R <% Request](request:R): Parameter = new Parameter(
      request.parameters.values.mkString(" ")
    )

    implicit def htmlSource[P <% Parameter](toWhom:P):HtmlResponse =
      HtmlResponse (
        html(head(title("Hello World")),
        body(h1("Hello " + toWhom.value)))
      )

    request:HtmlResponse
  }
}

