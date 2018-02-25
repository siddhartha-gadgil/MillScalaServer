import mill._, scalalib._, scalajslib._, define.Task
import ammonite.ops._

object shared extends Module{
  object jvm extends ScalaModule{
    def scalaVersion = "2.12.4"
    def millSourcePath = super.millSourcePath / up
  }

  object js extends ScalaJSModule {
    def scalaVersion = "2.12.4"
    def scalaJSVersion = "0.6.22"
    def millSourcePath = super.millSourcePath / up

    def platformSegment = "js"
  }
}

object server extends ScalaModule{
  def scalaVersion = "2.12.4"

  def moduleDeps = Seq(shared.jvm)
}

object client extends ScalaJSModule {
  def scalaVersion = "2.12.4"
  def scalaJSVersion = "0.6.22"
  def moduleDeps : Seq[ScalaJSModule] = Seq(shared.js)

  def platformSegment = "js"

  def generatedSources = T.sources{
    def deps = Task.traverse(moduleDeps)(_.sources)().flatten
    super.generatedSources() ++ deps
  }

}
