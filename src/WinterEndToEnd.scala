import org.eclipse.jetty.server.Server;

object WinterEndToEnd {
  def main(args:Array[String]) = {
    val server = new Server(9000)
    server.start()
    server.join();    
  }
}