package winter

object AnWinterApplication extends Winter {

  import hoops.Hoops._

  implicit def htmlSource[P <% ParameterObjects[String]](toWhom:P):HtmlResponse =
    HtmlResponse (
      html(head(title("Hello World")),
      body(h1("Hello " + toWhom.value)))
    )


  def process(request: Request) = {
    request:Response
  }
}
