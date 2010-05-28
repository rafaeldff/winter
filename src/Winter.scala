package winter
     
import _root_.hoops.Hoops

trait Response {
  def writeTo(sink:Sink)
}

trait Request {
  def parameters: scala.collection.Map[String,String] 
}

object Winter {
  case class TextResponse(text:String) extends Response {
    def writeTo(sink:Sink) = sink.writeBytes(text.getBytes)
  }

  case class HtmlResponse(element:Hoops.Element) extends Response {
    def writeTo(sink:Sink) = new TextResponse(element.toHtmlString).writeTo(sink)
  }

  def process(request:Request): Response = {
    import hoops.Hoops._

    def htmlSource(toWhom:String): Element = {
      html(head(title("Hello World")), body(h1("Hello " + toWhom)))
    }
    
    def parameterValues(request:Request): String = {
      request.parameters.values.mkString(" ")
    }
    
    ((parameterValues _) andThen (htmlSource _) andThen (HtmlResponse.apply _))(request)
  }
}

