package winter.samples.Quotes

import winter._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{Server, Request => JettyRequest}
import concurrent.SyncVar
import org.eclipse.jetty.util.component.LifeCycle.Listener
import org.eclipse.jetty.util.component.LifeCycle

object Main extends WebServer {
  val port = 8080
  
  def main(args:Array[String]) = startApplication(new Winter {
    def process(request: Request) = TextResponse("FooBar!?")
  })
}

trait WebServer {
  val port: Int;
  private[WebServer] var server: Server = _

  def startApplication(application: Winter): Unit = {
    startWebServer(WinterBootstrap.process(application))
  }

  private def configureServer(handler: (HttpServletRequest, HttpServletResponse) => Unit): Unit = {
    server setHandler new AbstractHandler {
      def handle(target: String, baseRequest: JettyRequest, request: HttpServletRequest, response: HttpServletResponse) = {
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

  def log(message: String) = println(message)

  def shutDownWebServer() = {
    log("Shutting down...")
    server.stop
    log("Shutdown complete")
  }

  class AbstractListener extends Listener {
    def lifeCycleStopped(lifeCycle: LifeCycle) = {}

    def lifeCycleStopping(lifeCycle: LifeCycle) = {}

    def lifeCycleFailure(lifeCycle: LifeCycle, p2: Throwable) = {}

    def lifeCycleStarted(lifeCycle: LifeCycle) = {}

    def lifeCycleStarting(lifeCycle: LifeCycle) = {}
  }

}