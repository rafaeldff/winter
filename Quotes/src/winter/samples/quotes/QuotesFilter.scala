package winter.samples.quotes

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import winter.WinterBootstrap
import winter.samples.Quotes.QuotesApplication
import javax.servlet._
import java.net.URL

class QuotesFilter extends Filter {
  var context: Option[ServletContext] = None
  def init(config: FilterConfig) = context = Some(config.getServletContext)

  def destroy = {}

  def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) =
    (servletRequest, servletResponse) match {
      case (request:HttpServletRequest, response: HttpServletResponse) =>
        if (requestingStaticResource(request))
          chain.doFilter(request, response)
      else
        WinterBootstrap.process(QuotesApplication)(request,response)
    }

  private def requestingStaticResource(request:HttpServletRequest):Boolean = {
    val resourceUrl = context.get.getResource(uriRelativeToContextRoot(request))
    return resourceUrl != null && isAFile(resourceUrl)
  }

  def uriRelativeToContextRoot(request: HttpServletRequest) =
     request.getRequestURI().substring(request.getContextPath().length());

  def isAFile(resourceUrl: URL) = !resourceUrl.toString().endsWith("/")
}