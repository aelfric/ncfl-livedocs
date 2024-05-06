import java.io.FileReader
import org.apache.commons.csv.CSVFormat
import java.io.Reader
import org.apache.commons.csv.CSVParser
import scala.jdk.CollectionConverters._
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main extends App {
  val in: Reader = new FileReader("2024-schedule.csv");
  val df: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mma")

  val records: CSVParser = CSVFormat.EXCEL.parse(in);

  var lastTime: LocalTime = LocalTime.of(0, 0)

  val rawSched = for {
    rec <- records.asScala
  } yield {
    val time = LocalTime.parse(rec.get(0).replaceAll(" +", ""), df)
    val events = rec
      .get(2)
      .split(",")
      .toList
      .map(_.trim())
      .map(_.split(" ", 2) match {
        case Array(cat, evt) => Event(cat, evt)
      })
    Schedule(time, events)
  }

  rawSched map println

  val fullSched = rawSched flatMap { case Schedule(time, events) =>
    events map (e => Schedule(time, List(e)))
  }

  val cx = filterSched(fullSched, "CX")
  val ld = filterSched(fullSched, "LD")
  val pf = filterSched(fullSched, "PF")
  val con = filterSched(fullSched, "CON")

  val dec = filterSched(fullSched, "DEC")
  val duo = filterSched(fullSched, "DUO")
  val dp = filterSched(fullSched, "DP")
  val ext = filterSched(fullSched, "EXT")
  val oo = filterSched(fullSched, "OO")
  val oi = filterSched(fullSched, "OI")

  println("Lincoln Douglas")
  debateToMarkdown(ld)
  println("Policy Debate")
  debateToMarkdown(cx)
  println("Public Forum")
  debateToMarkdown(pf)

  con map println

  /*
   *

  dec map println
  duo map println
  dp map println
  ext map println
  oo map println
  oi map println
   */
  speechToMarkdown(List(dec, duo, dp, ext, oo, oi))

  case class Schedule(time: LocalDateTime, events: List[Event])
  case class Event(category: String, round: String)
  def filterSched(fullSched: Iterable[Schedule], category: String) =
    fullSched filter (s =>
      s.events.headOption.map(_.category).contains(category)
    )

  def debateToMarkdown(sched: Iterable[Schedule]) =
    for {
      s <- sched
      e <- s.events
    } yield {
      println(s"| ${e.round} | ${df.format(s.time.minusMinutes(30))}| ${df
          .format(s.time.minusMinutes(10))}| ${df.format(s.time)} | ")
    }

  def speechToMarkdown(scheds: List[Iterable[Schedule]]) =
    (scheds.flatten) groupBy (_.time) map println
}
