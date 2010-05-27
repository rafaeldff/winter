package winter

import _root_.hoops.Hoops
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import collection.JavaConversions

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
    
    HtmlResponse(htmlSource(request.parameters.values.mkString(" ")));
  }
}

