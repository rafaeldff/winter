package winter

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

object WinterBootstrap {
  def process(app:Winter)(request:HttpServletRequest, httpResponse:HttpServletResponse) = {
    val response = app.process(new ServletApiRequest(request))
    val output = new Output(httpResponse)
    response.writeTo(output.stream)
  }
}