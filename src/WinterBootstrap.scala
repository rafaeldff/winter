package winter

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

object WinterBootstrap {
  def process(request:HttpServletRequest, response:HttpServletResponse) = {
    Winter.process(new Request(request), new Response(response))
  }
}