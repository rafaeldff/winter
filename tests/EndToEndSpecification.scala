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
import org.eclipse.jetty.server.{Request => JettyRequest, Server}

class EndToEndSpecification extends Specification with WebServer with WebClient {
  val port = 9000
  val baseURI = "http://localhost:9000/"
  
  def startApplication(application: Winter): Unit = {
    startWebServer(WinterBootstrap.process(application))
  }
  
  "Winter" should {                                       
    "run a simple web application" in {
      doBefore(startWebServer(WinterBootstrap.process(AnWinterApplication)))

      "servicing a simple web request" in {
        val result = get(baseURI)
        result must_== "<html><head><title>Hello World</title></head><body><h1>Hello </h1></body></html>"
      }

      "echoing request parameters" in {
        val result = post(baseURI, "pName1=parameterValue1&pName2=parameterValue2")
        result must (include("parameterValue1") and include("parameterValue2"))
      }
      
      doAfter(shutDownWebServer)
    }

    "handle several web applications" in {
      "running one application" in {
        startApplication(new Winter {
          def process(request: Request) = TextResponse("some response")
        })

        get(baseURI) must_== "some response"
      }

      "running another application" in {
        startApplication(new Winter {
          def process(request: Request) = TextResponse("different response")
        })

        get(baseURI) must_== "different response"
      }

      doAfter(shutDownWebServer)      
    }

    "enable routing" in {
        "based on whole paths" in {
          object ApplicationWithTwoPaths extends Winter {
            def process(request: Request):Response = request match {
              case winter.Path("pathA") => TextResponse("from a")
              case winter.Path("pathB") => TextResponse("from b")
            }
          }          
          startApplication(ApplicationWithTwoPaths)

          get(baseURI + "pathA") must_== "from a"
          get(baseURI + "pathB") must_== "from b"
        }

        doAfter(shutDownWebServer)
      }


    "work with marshalled request parameters" in {
      skip("implementing...")
      doAfter(shutDownWebServer)
      
      startApplication(ApplicationReadingParameters)
      get(baseURI + "?foo.str=asdf") must_== "got asdf"
    }
  }
}

object ApplicationReadingParameters extends Winter {
  implicit def withFoo[P <% ParameterObjects[Foo]](param:P):Response= new TextResponse(param.value.str)

  def process(request: Request) =  {
    request:Response
  }
}

case class Foo(str:String)

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

  def get(uri: String) = {
    val url = new URL(uri)
    readResponse(url)
  }

  def post(uri: String, body: String) = {
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
  private[WebServer] var server: Server = _

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

    def lifeCycleStarted(lifeCycle: LifeCycle) = {

    }

    def lifeCycleStarting(lifeCycle: LifeCycle) = {}
  }

}