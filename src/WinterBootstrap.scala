package winter

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

object WinterBootstrap {
  def process(request:HttpServletRequest, httpResponse:HttpServletResponse) = {
    val response = Winter.process(new ServletApiRequest(request))
    val output = new Output(httpResponse)
    response.writeTo(output.stream)
  }
}