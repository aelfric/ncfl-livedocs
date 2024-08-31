import spandoc._
import ast._
import cats.Id
import cats.effect.{IO, Resource}
import transform._

import scala.io.{BufferedSource, Source}

object IncludeFilterMain {
  private def openFile(str: String): IO[BufferedSource] =
    IO(Source.fromFile(str))

  private def readLines(src: BufferedSource) = IO(src.getLines().mkString("\n"))

  private def closeFile(file: BufferedSource) = {
    IO(file.close())
  }

  val uppercase: TopDown[Id] = TopDown.inline { case Str(str) =>
    Str(str.toUpperCase)
  }

  val include: TopDown[IO] = TopDown.blockM { case CodeBlock(attr, text) =>
    val maybeString = attr.attr.toMap.get("include")
    val str = maybeString
    if (str.isEmpty) {
      IO(CodeBlock(attr, text))
    } else {
      val resource = Resource.make(openFile(str.get))(file => closeFile(file))
      resource.use(src => {
        readLines(src) map (txt => Plain(Vector(Str(txt))))
      })
    }
  }

  def main(args: Array[String]): Unit = {
    transformStdin(uppercase)
  }

}
