package winter
package samples.Quotes

case class Quote(author:String, text:String)

object QuotesApplication extends Winter {
  var sampleQuotes = List(
    Quote("A computational process is indeed much like a sorcerer's idea of a spirit. It cannot be seen or touched. It is not composed of matter at all. However, it is very real. ", "Hal Abelson"),
    Quote("Beware of the Turing tar-pit where everything is possible but nothing of interest is easy.", "Alan Perlis"),
    Quote("We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil", "Don Knuth"),
    Quote("The string is a stark data structure and everywhere it is passed there is much duplication of process. It is a perfect vehicle for hiding information.", "Alan Perlis")
  )

  import hoops.tc.Html4Strict._

  def process(request: Request) = HtmlResponse(
    html(
      head(
        title("Quotes") ::
        link(rel("stylesheet"), href("/quotes.css")) ::
        ENil
      ),
      body(
        div(cssClass("main"))(
          h1("QUOTES") ::
          quotesList ::
          fieldset(form(Action("newQuote"))(
            ol(
              li(text("quote")),
              li(input(cssClass("in"), name("author"))),
              li(submit("ok"))
            ) :: ENil
          )) ::
          ENil
        )
      ))
  )

  def submit(buttonLabel:String) = input(inputType("submit"), value(buttonLabel), cssClass("submit"))

  def control(inputType: String)(controlName: String) =
    label(forElement(controlName))(controlName.capitalize+":") :: input(id(controlName), cssClass("in"), name(controlName)) :: ENil

  def text(controlName: String) =
    textarea(id(controlName), cssClass("in"), name(controlName))("")

  def quotesList = sampleQuotes map {
    case Quote(text, author) => div(
      blockquote(p(text)) ::
      p(cssClass("author"))(author) :: ENil
    )}
}

