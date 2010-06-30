package winter

object ASimpleWinterApplication extends Winter {

  import hoops.Hoops._

  implicit def htmlSource[R <% Request](r:R):HtmlResponse =
    HtmlResponse (
      html(head(title("Hello World")),
      body(h1("Hello Nasty World!")))
    )


  def process(request: Request) = {
    request:Response
  }
}
