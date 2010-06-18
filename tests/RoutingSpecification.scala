package winter.routingTest

import _root_.winter.{Request, Path => WinterPath}
import org.specs.Specification
import org.specs.matcher.Matcher


class RoutingSpecification extends Specification {
  
  def requestForUri(theUri: String): Request = {
    new Request {
      def parameters = null

      def uri: String = theUri
    }
  }

  "Path extractor" should {
    "match a single path component against a single path component URI" in {
      val request = requestForUri("/pathComponent")
      request must beLike {
        case WinterPath("pathComponent") => true
      }
    }

    "match multiple path components agaisnt a multiple path component URI" in {
      val request = requestForUri("/first/second")
      request must beLike {
        case WinterPath("first", "second") => true
      }
    }

    "not match a path component agaist an URI with a differing path component" in {
      val request = requestForUri("/someOtherThing");
      request must not(beLike {
        case WinterPath("somePath") => true
      })
    }
    
    "not match empty pattern agains nonempty URI" in {
      val request = requestForUri("/something");
      request must not(beLike {
        case WinterPath() => true
      })
    }

    "match a prefix of an URI" in {
      val request = requestForUri("/the/prefix/the/rest/")
      request must beLike {
        case WinterPath("the", "prefix", rest@_*) => rest must_== Seq("the", "rest") 
      }
    }

    "capture a path segment" in {
      val request = requestForUri("/before/42/after/")
      request must beLike {
        case WinterPath("before", segment, "after") => segment.toInt must_== 42
      }
    }
  }
  
  
}