package winter.samples.Quotes

import winter._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import concurrent.SyncVar
import org.eclipse.jetty.util.component.LifeCycle.Listener
import org.eclipse.jetty.util.component.LifeCycle
import org.eclipse.jetty.server.{Server, Request => JettyRequest}
import org.eclipse.jetty.server.handler.{DefaultHandler, HandlerList, ResourceHandler, AbstractHandler}
import org.eclipse.jetty.webapp.WebAppContext

object Main extends WebServer {
  val port = 8080

  def main(args:Array[String]) = {
    startApplication(QuotesApplication)
  }
}

trait WebServer {
  val port: Int;
  private[WebServer] var server: Server = _

  def startApplication(application: Winter): Unit = {
    startWebServer(WinterBootstrap.process(application))
  }

  private def configureServer(handler: (HttpServletRequest, HttpServletResponse) => Unit): Unit = {
//    val winterHandler = new AbstractHandler {
//      def handle(target: String, baseRequest: JettyRequest, request: HttpServletRequest, response: HttpServletResponse) = {
//        response.setStatus(HttpServletResponse.SC_OK)
//
//        try {handler(request, response)}
//        finally {baseRequest.setHandled(true)}
//      }
//    }
//    val staticFilesHandler = new ResourceHandler
//    staticFilesHandler.setResourceBase("./webapp")
//
//    val handlerList = new HandlerList
//    handlerList.setHandlers(Array(staticFilesHandler, winterHandler, new DefaultHandler))

    val webAppContext = new WebAppContext();
    webAppContext.setDescriptor("./webapp/WEB-INF/web.xml");
    webAppContext.setResourceBase("./webapp");
    webAppContext.setContextPath("/");
    webAppContext.setParentLoaderPriority(true);


    server setHandler webAppContext
  }

  def setupCurrentDir() = {
    import java.io._
    val current = new File(".")
    if (!current.getAbsolutePath.contains("Quotes")) {
      val quotesDir = new File(current, "Quotes")
      if (quotesDir.exists)
        System.setProperty("user.dir", quotesDir.getAbsolutePath)
    }

  }

  def startWebServer(handler: (HttpServletRequest, HttpServletResponse) => Unit): Unit = {
    setupCurrentDir()

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