import org.apache.commons.csv.{CSVFormat, CSVParser}

import java.io.{FileReader, Reader}
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, LocalTime, MonthDay}
import scala.jdk.CollectionConverters._

object Main extends App {
  private val in: Reader = new FileReader("2024-schedule.csv")
  private val tf: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mma")
  private val df: DateTimeFormatter = DateTimeFormatter.ofPattern("d-MMM")

  println(df.format(LocalDateTime.now()))

  private val records: CSVParser = CSVFormat.EXCEL.parse(in)

  private val rawSched = for {
    rec <- records.asScala
  } yield {
    val date = MonthDay.parse(rec.get(0), df)
    val time = LocalTime.parse(rec.get(1).replaceAll(" +", ""), tf)
    val events = rec
      .get(3)
      .split(",")
      .toList
      .map(_.trim())
      .map(_.split(" ", 2) match {
        case Array(cat, evt) => Event(cat, evt)
      })
    Schedule(date.atYear(2024).atTime(time), events)
  }

  // TODO lunch breaks
  // TODO finals meetings
  // TODO speech meeting
  // TODO congress schedule on website?

  rawSched map println

  private val fullSched = rawSched flatMap { case Schedule(time, events) =>
    events map (e => Schedule(time, List(e)))
  }

  val cx = filterSched(fullSched, "CX")
  val ld = filterSched(fullSched, "LD")
  val pf = filterSched(fullSched, "PF")
  val con = filterSched(fullSched, "CON")

  val dec = filterSched(fullSched, "DEC")
  val duo = filterSched(fullSched, "DUO")
  val dp = filterSched(fullSched, "DP")
  val ext = filterSched(fullSched, "EX")
  val extPrep = ext.map( s => Schedule(s.time.minusMinutes(30).plusNanos(1000), s.events.map(e => Event(e.category, s"${e.round} (Draw)"))))
  val oo = filterSched(fullSched, "OO")
  val oi = filterSched(fullSched, "OI")

  println("Lincoln Douglas")
  debateToMarkdown(ld)
  println("Policy Debate")
  debateToMarkdown(cx)
  println("Public Forum")
  debateToMarkdown(pf)

  con map println

  speechToMarkdown(List(dec, duo, dp, oo, oi, ext, extPrep))

  case class Schedule(time: LocalDateTime, events: List[Event])
  case class Event(category: String, round: String){
    override def toString: String = s"$category $round"
  }
  private def filterSched(fullSched: Iterable[Schedule], category: String) =
    fullSched filter (s =>
      s.events.headOption.map(_.category).contains(category)
    )

  private def debateToMarkdown(sched: Iterable[Schedule]) =
    for {
      s <- sched
      e <- s.events
    } yield {
      println(s"| ${e.round} | ${tf.format(s.time.minusMinutes(30))}| ${tf
          .format(s.time.minusMinutes(10))}| ${tf.format(s.time)} | ")
    }

  private def speechToMarkdown(scheds: List[Iterable[Schedule]]) = {
    val timeToSchedules = scheds.flatten groupBy (_.time)
    for {
      i <- timeToSchedules.toSeq.sortBy(_._1)
    } yield {
      println(s"${tf.format(i._1)} | ${i._2.flatMap(_.events).mkString(", ")}")
    }
  }
}
