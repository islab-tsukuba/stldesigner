import scala.io.Source

case class LisFile(path: String, conf: Config) {
  val content = Source.fromFile(path).getLines().toSeq

  def getVoltage(pointName: String): Seq[Double] = {
    val dataLines = getDataLines()
    val typeHeader = dataLines.head.split("""\s+""")
    val nameHeader = "time" +: dataLines(1).split("""\s+""")
    val pointIndex = typeHeader.zip(nameHeader).map {
      case (dataType, name) =>
        (dataType == "voltage") && (name == pointName)
      case _ => false
    }.indexOf(true)
    val mainLines = dataLines.slice(2, dataLines.size)
    mainLines.map(line => UnitUtil.strToDouble(line.split("""\s+""")(pointIndex)).getOrElse(0.0))
  }

  def getDataLines(): Seq[String] = {
    val start = content.indexOf("x")
    val end = content.indexOf("y")
    content.slice(start + 2, end)
  }
}
