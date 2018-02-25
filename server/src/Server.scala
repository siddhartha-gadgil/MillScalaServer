package server

import shared.Hello._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object Server extends App{
  println(sayHello("server"))

  val indexHTML =
s"""
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>temporary</title>
  </head>
  <body>

    <p>${sayHello("jvm server")}</p>
    <div id="blank">

    </div>
    <script type="text/javascript" src="/public/out.js">

    </script>
  </body>
</html>
"""

  implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
      path("index.html") {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, indexHTML))
      } ~
      path("public" / Segment){ name =>
        getFromResource(name.toString)
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
}
