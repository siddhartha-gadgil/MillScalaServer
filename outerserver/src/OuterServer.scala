package server

import shared.Hello._


import io.undertow.websockets.WebSocketConnectionCallback
import io.undertow.websockets.core.{AbstractReceiveListener, BufferedTextMessage, WebSocketChannel, WebSockets}
import io.undertow.websockets.spi.WebSocketHttpExchange

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object OuterSubServer extends cask.Routes{

  @cask.get("/outer.html")
  def hello(): String = Home.indexHTML

  // @cask.staticResources("/public")
  // def staticResourceRoutes() = "."
  //
  @cask.websocket("/outer/connect")
  def showUserProfile(): cask.WebsocketResult = {
    new WebSocketConnectionCallback() {
      override def onConnect(exchange: WebSocketHttpExchange, channel: WebSocketChannel): Unit = {
        channel.getReceiveSetter.set(
          new AbstractReceiveListener() {
            override def onFullTextMessage(channel: WebSocketChannel, message: BufferedTextMessage): Unit = {
              message.getData match{
                case "" => channel.close()
                case data =>
                  Future{Home.slow(channel)}
                  WebSockets.sendTextBlocking(data, channel)
              }
            }
          }
        )
        channel.resumeReceives()
      }
    }
  }

  initialize()

}

object OuterServer extends cask.Main(OuterSubServer, SubServer)


object Home{
  val indexHTML: String =
    s"""
       |<!DOCTYPE html>
       |<html>
       |  <head>
       |    <meta charset="utf-8">
       |    <title>Mill with Scala-js and server</title>
       |  </head>
       |  <body>
       |
       |    <p>${sayHello("jvm server")}</p>
       |    <div id="blank">
       |
       |    </div>
       |    <script type="text/javascript" src="/public/out.js">
       |
       |    </script>
       |  </body>
       |</html>
     """.stripMargin

  def slow(channel: WebSocketChannel): Unit = {
    Thread.sleep(2000)
    WebSockets.sendTextBlocking("woken up websocket", channel)
  }
}
