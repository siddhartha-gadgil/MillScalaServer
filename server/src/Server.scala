package server

import shared.Hello._


object Server extends cask.MainRoutes{

  @cask.get("/")
  def hello() = indexHTML

  @cask.staticResources("/public")
  def staticResourceRoutes() = "."

  initialize()

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
}
