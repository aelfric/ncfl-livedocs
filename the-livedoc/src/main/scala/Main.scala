import scala.language.implicitConversions
import scalatags.Text.all._
import scalatags.Text
sealed trait DayOfWeek
case object Friday extends DayOfWeek
case object Saturday extends DayOfWeek
case object Sunday extends DayOfWeek

case class TimeSlot(
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    day: DayOfWeek
) {
  def toHtmlString: Text.TypedTag[String] = {
    val a = "%02d".formatted(startHour)
    val b = "%02d".formatted(startMinute)
    val c = "%02d".formatted(endHour)
    val d = "%02d".formatted(endMinute)

    span(s"$a:$b - $c:$d")
  }
}

sealed trait Event

case object Congress extends Event

case object LincolnDouglas extends Event
case object PublicForum extends Event
case object PolicyDebate extends Event

sealed trait Speech extends Event
case object Declamation extends Speech
case object Extemp extends Speech
case object Oratory extends Speech
case object OralInterp extends Speech
case object DramaticPerformance extends Speech
case object DuoInterp extends Speech

sealed trait CompetitionSite

case object UnivOfLouisville extends CompetitionSite
case object DuPontManual extends CompetitionSite

case class Something(
    event: Event,
    site: List[CompetitionSite],
    label: String,
    time: TimeSlot
) {
  def toHtmlRow = {
    tr(
      td(time.toHtmlString),
      td(label),
      td(site.toString())
    )
  }
}

object Main extends App {
  var schedule = List(
    Something(
      Congress,
      List(UnivOfLouisville),
      "Session 1",
      TimeSlot(
        9,
        0,
        10,
        0,
        Saturday
      )
    ),
    Something(
      Congress,
      List(UnivOfLouisville),
      "Session 2",
      TimeSlot(
        11,
        0,
        12,
        0,
        Saturday
      )
    ),
    Something(
      Extemp,
      List(UnivOfLouisville),
      "Round 1",
      TimeSlot(
        9,
        0,
        10,
        0,
        Saturday
      )
    )
  )
  println(table(tbody(schedule.filter(_.event == Congress).map(_.toHtmlRow))))
  println(table(tbody(schedule.filter(_.event == Extemp).map(_.toHtmlRow))))
}
