import mill._, scalalib._, scalajslib._, define.Task
import ammonite.ops._

object shared extends Module{
  object jvm extends ScalaModule{
    def scalaVersion = "2.12.6"
    def millSourcePath = super.millSourcePath / up
  }

  object js extends ScalaJSModule {
    def scalaVersion = "2.12.6"
    def scalaJSVersion = "0.6.22"
    def millSourcePath = super.millSourcePath / up

    def platformSegment = "js"
  }
}

object server extends ScalaModule{
  def scalaVersion = "2.12.6"

  def moduleDeps = Seq(shared.jvm)

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.1.9"
  )

  def resources = T.sources {
    def base : Seq[Path] = super.resources().map(_.path)
    def jsout = client.fastOpt().path / up
    (base ++ Seq(jsout)).map(PathRef(_))
  }

  def mainClass = Some("server.Server")
}

object client extends ScalaJSModule {
  def scalaVersion = "2.12.6"
  def scalaJSVersion = "0.6.22"
  def moduleDeps : Seq[ScalaJSModule] = Seq(shared.js)

  def platformSegment = "js"

  import coursier.maven.MavenRepository

  def repositories = super.repositories ++ Seq(
    MavenRepository("https://oss.sonatype.org/content/repositories/releases")
  )

  def ivyDeps = Agg(
    ivy"org.scala-js::scalajs-dom::0.9.4"
  )

}
