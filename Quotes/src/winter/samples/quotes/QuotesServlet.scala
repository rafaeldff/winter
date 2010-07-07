package winter.samples.quotes

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import winter.WinterBootstrap
import winter.samples.Quotes.QuotesApplication

class QuotesServlet extends HttpServlet {
  override def service(req: HttpServletRequest, resp: HttpServletResponse) = {
    WinterBootstrap.process(QuotesApplication)(req,resp)    
  }
}