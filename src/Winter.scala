package winter

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

object Winter {
  def process(request:Request, response:Response) = {
    response.body("""<h1>Hello</h1>""");
  }
}

class Request(httpServletRequest:HttpServletRequest)
class Response(httpServletResponse:HttpServletResponse) {
  def body(what:String) = httpServletResponse.getWriter.println(what)
}