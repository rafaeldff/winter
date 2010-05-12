package winter

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

object Winter {
  def process(request:Request, response:Response) = {
    response.body("""
      <head><title>yay</title></head>
      <body>
      <h1>Sup!</h1>
      </body>
     """);
  }
}

class Request(httpServletRequest:HttpServletRequest)
class Response(httpServletResponse:HttpServletResponse) {
  def body(what:String) = httpServletResponse.getWriter.println(what)
}