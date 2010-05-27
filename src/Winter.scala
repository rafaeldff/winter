package winter

import _root_.hoops.Hoops
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import collection.JavaConversions

object Winter {
  def process(request:Request, response:Response) = {
    def htmlSource: String = {
      import hoops.Hoops._
      html(head(title("Hello World")), body(h1("Hello %s"))) 
    }.toHtmlString
    
    response.body(htmlSource format request.parameters.values.mkString(" "));
  }
}

