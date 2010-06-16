package winter.routingTest

import _root_.winter.{Request, Path => WinterPath}
import org.specs.Specification

class RoutingSpecification extends Specification {
  
  def requestForUri(theUri: String): Request = {
    new Request {
      def parameters = null

      def uri: String = theUri
    }
  }
  
  "Path extractor" should {
    "match a single path component string URI" in {
      val request = requestForUri("/pathComponent")
      WinterPath.unapply(request) must beSome("pathComponent")
    }
  }
  
  
}