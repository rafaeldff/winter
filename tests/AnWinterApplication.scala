package winter

object AnWinterApplication extends Winter {

  def process(request:Request): Response = {
    import hoops.Hoops._

    implicit def htmlSource[P <% Parameter[String]](toWhom:P):HtmlResponse =
      HtmlResponse (
        html(head(title("Hello World")),
        body(h1("Hello " + toWhom.value)))
      )

    request:HtmlResponse
  }
}
