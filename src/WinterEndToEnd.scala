import _root_.winter.WinterBootstrap
import concurrent.SyncVar
import java.lang.{Throwable, String}
import java.util.Scanner
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.{Request, Server}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.util.component.LifeCycle
import org.eclipse.jetty.util.component.LifeCycle.Listener

object WinterEndToEnd extends WebServer {
  val port = 9000

  def main(arguments:Array[String]) = {
    shouldReturnHello
  }

  def shouldReturnHello: Unit = {
    startWebServer(WinterBootstrap.process)
    val result = makeRequest("http://localhost:9000/")
    try {
      assert(result == "<h1>Hello</h1>\n", "Got %s".format(result))
    }
    finally {
      shutDownWebServer()
    }
  }

  def makeRequest(uri:String) = {
    val url = new java.net.URL(uri)
    val scanner = new Scanner(url.openStream)
    scanner.useDelimiter("\\z")
    scanner.next
  }

}

trait WebServer {
  val port: Int;
  private[WebServer] var server:Server = _

  private def configureServer(handler: (HttpServletRequest, HttpServletResponse) => Unit): Unit = {
    server setHandler new AbstractHandler {
      def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) = {
        response.setStatus(HttpServletResponse.SC_OK)
        response.setContentType("text/html;charset=utf-8")

        try {handler(request, response)}
        finally {baseRequest.setHandled(true)}
      }
    }
  }

  def startWebServer(handler: (HttpServletRequest, HttpServletResponse) => Unit): Unit = {
    log("Starting server")

    val done = new SyncVar[Unit]
    
    server = new Server(port)
    server addLifeCycleListener new AbstractListener {
      override def lifeCycleStarted(lifeCycle: LifeCycle) = {
        done set ()          
      }
    }
    configureServer(handler)
    server.start()

    done.get
    log("Server started")
  }

  def log(message:String ) = println(message)

  def shutDownWebServer() = {
    log("Shutting down...")
    server.stop
    log("Shutdown complete")
  }

  class AbstractListener extends Listener {
      def lifeCycleStopped(lifeCycle: LifeCycle) = {}
      def lifeCycleStopping(lifeCycle: LifeCycle) = {}
      def lifeCycleFailure(lifeCycle: LifeCycle, p2: Throwable) = {}
      def lifeCycleStarted(lifeCycle: LifeCycle) = {

      }
      def lifeCycleStarting(lifeCycle: LifeCycle) = {}
    }

}