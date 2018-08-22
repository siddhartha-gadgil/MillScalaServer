package client

import scala.scalajs.js.annotation._
import shared.Hello
import org.scalajs.dom

import org.scalajs.dom.raw._
import scala.scalajs.js


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

  val chat = new WebSocket(s"ws://${dom.document.location.host}/connect")

  chat.onopen = {
    (event: Event) =>
      chat.send("websocket")
  }

  chat.onmessage = {(event: MessageEvent) =>
    val msg = event.data.toString
    val child: Element = dom.document
      .createElement("div")

    child.textContent =
      Hello.sayHello(msg)
    blank.appendChild(child)

  }


}
