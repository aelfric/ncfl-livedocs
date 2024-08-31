import cats.effect.unsafe.implicits.global
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spandoc.ast._

class IncludeFilterMainTest extends AnyFlatSpec with Matchers {
  it should "something" in {
    IncludeFilterMain.uppercase(Str("header")) shouldBe Str("HEADER")
  }

  it should "something else" in {
    IncludeFilterMain.include(Str("header")).unsafeRunSync() shouldBe Str("header")
    IncludeFilterMain.include(
      CodeBlock(Attr("", List.empty, List("include" -> "general.md")), "header")
    ).unsafeRunSync() shouldBe Plain(Vector(Str("general.md")))
  }
}
