package winter.samples.Quotes

import _root_.winter._
import hoops.Hoops

case class Quote(author:String, text:String)

object QuotesApplication extends Winter {
  val sampleQuotes = List(
    Quote("A computational process is indeed much like a sorcerer's idea of a spirit. It cannot be seen or touched. It is not composed of matter at all. However, it is very real. ", "Hal Abelson"),
    Quote("Beware of the Turing tar-pit where everything is possible but nothing of interest is easy.", "Alan Perlis"),
    Quote("We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil", "Don Knuth"),
    Quote("The string is a stark data structure and everywhere it is passed there is much duplication of process. It is a perfect vehicle for hiding information.", "Alan Perlis")
  )

  import Hoops._
  
  def process(request: Request) = HtmlResponse(
    html(
      head(
        title("Quotes"),
        link(rel("stylesheet"), href("/quotes.css"))
        ),
      body(
        (h1("Quotes") :: quotesList):_*
      )))

  def quotesList = sampleQuotes map {
    case Quote(text, author) => div(
      blockquote(p(text)),
      p(author)
    )
  }
}

