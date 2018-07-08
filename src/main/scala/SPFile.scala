import scala.io.Source

case class SPFile(path: String, config: Config) {
  val firstSPFileContent: List[String] = Source.fromFile(path).getLines.toList
  var stlElements: List[STLElement] = List()

  def getSTLElements(): List[STLElement] = {
    firstSPFileContent.zipWithIndex.filter {
      case (line, _) => line.matches("""^.[0-9]+_STL_[0-9]+.*""")
    }.map {
      case (line, i) => {
        line(0) match {
          case 'W' => STLWElement(line, i, config)
          case _ => throw new RuntimeException("Invalid STL element is found.")
        }
      }
    }
  }

  def setSTLElements(elements: List[STLElement]): Unit = {
    stlElements = elements
  }

  def getString(): String = {
    firstSPFileContent.zipWithIndex.flatMap {
      case (line, i) =>
        var ret: List[String] = List(line)
        for (stlElement <- stlElements) {
          if (stlElement.index == i) ret = stlElement.getElementLines()
        }
        ret
    }.mkString("\n")
  }
}
