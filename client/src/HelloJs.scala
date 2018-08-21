package client

import scala.scalajs.js.annotation._
import shared.Hello
import org.scalajs.dom

import org.scalajs.dom.raw.Element

@JSExportTopLevel("HelloJS")
object HelloJS {
  @JSExport
  def helloJs: String = Hello.sayHello("scala-js")
  def blank: Element = dom.document.querySelector("#blank")

  val child: Element = dom.document
                 .createElement("div")

  child.textContent =
    Hello.sayHello("js")
  blank.appendChild(child)
}
