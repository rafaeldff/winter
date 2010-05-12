import _root_.winter.WinterBootstrap
import java.lang.String
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.{Request, Handler, Server}
import org.eclipse.jetty.server.handler.AbstractHandler

object WinterEndToEnd {
  def main(args:Array[String]) = {
    val server = new Server(9000)
    server setHandler new AbstractHandler {
      def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) = {
        response.setStatus(HttpServletResponse.SC_OK)
        response.setContentType("text/html;charset=utf-8")

        WinterBootstrap.process(request, response);

        baseRequest.setHandled(true)
      }
    }
    server.start()
    server.join();    
  }
}