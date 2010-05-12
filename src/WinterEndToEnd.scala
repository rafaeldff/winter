import java.lang.String
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.{Request, Handler, Server}
import org.eclipse.jetty.server.handler.AbstractHandler
object WinterEndToEnd {
  def main(args:Array[String]) = {
    val server = new Server(9000)
    server setHandler new AbstractHandler {
      def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) = {
        response.setContentType("text/html;charset=utf-8")
        response.setStatus(HttpServletResponse.SC_OK)
        response.getWriter.println("<h1>Hello!</h1>")
        baseRequest.setHandled(true)
      }
    }
    server.start()
    server.join();    
  }
}