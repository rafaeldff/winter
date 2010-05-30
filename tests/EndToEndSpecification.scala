package winter

import concurrent.SyncVar
import java.io.{InputStream, PrintWriter, OutputStreamWriter}
import java.lang.{Throwable, String}
import java.net.{HttpURLConnection, URL}
import java.util.Scanner
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.util.component.LifeCycle
import org.eclipse.jetty.util.component.LifeCycle.Listener
import org.specs.{Specification}
import org.eclipse.jetty.server.{Request=>JettyRequest, Server}

class EndToEndSpecification extends Specification with WebServer with WebClient {
  val port = 9000

  "An Winter Application" should {
    doBefore(startWebServer(WinterBootstrap.process(AnWinterApplication)))
    doAfter(shutDownWebServer)

    "service a simple web request" in {
      val result = doGET("http://localhost:9000/")
      result must_== "<html><head><title>Hello World</title></head><body><h1>Hello </h1></body></html>"
    }

    "echo request parameters" in {
      val result = doPOST("http://localhost:9000/", "pName1=parameterValue1&pName2=parameterValue2")
      result must (include("parameterValue1") and include("parameterValue2"))
    }
  }

  "Another Winter Application" should {
    doBefore(startWebServer(WinterBootstrap.process(new Winter {
      def process(request: Request) = TextResponse("different response")
    })))
    doAfter(shutDownWebServer)
    
    "service a different web request" in {
      val result = doGET("http://localhost:9000/")
      result must_== "different response"
    }
  }


}

trait WebClient {
  def readFromInputStream(inputStream: InputStream): String = {
    try {
      val scanner = new Scanner(inputStream)
      scanner.useDelimiter("\\z")
      scanner.next
    }
    finally {
      inputStream.close()
    }
  }

  def readResponse(url: URL): String = {
    val inputStream: InputStream = url.openStream
    readFromInputStream(inputStream)
  }

  def doGET(uri:String) = {
    val url = new URL(uri)
    readResponse(url)
  }

  def doPOST(uri:String, body:String) = {
    val url = new URL(uri)
    val cnn = url.openConnection.asInstanceOf[HttpURLConnection]
    cnn.setDoOutput(true)

    val out = new PrintWriter(new OutputStreamWriter(cnn.getOutputStream))
    out.print(body)
    out.close()

    readFromInputStream(cnn.getInputStream)
  }
}

trait WebServer {
  val port: Int;
  private[WebServer] var server:Server = _

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