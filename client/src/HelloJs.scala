package client
import scala.scalajs._
import scala.scalajs.js.annotation._

import shared.Hello._
// import org.scalajs.dom
// import org.scalajs.dom.html

@JSExportTopLevel("HelloJS")
object HelloJS {
  @JSExport
  def helloJs = sayHello("js")
}
