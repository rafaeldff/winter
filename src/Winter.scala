package winter

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import collection.JavaConversions

object Winter {
  def process(request:Request, response:Response) = {
    response.body("""<h1>Hello %s</h1>""" format request.parameters.values.mkString(" "));
  }
}

