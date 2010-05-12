import org.specs.Specification
import org.specs.util.SimpleTimer

/**
 * Created by IntelliJ IDEA.
 * User: rafaelf
 * Date: May 12, 2010
 * Time: 3:25:13 PM
 * To change this template use File | Settings | File Templates.
 */

object FooBar extends Specification {
  "something" should {
    "fail" in {
      true must beFalse
    }
  }
}