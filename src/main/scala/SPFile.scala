import scala.io.Source

case class SPFile(path: String) {
  val firstSPFileContent: List[String] = Source.fromFile(path).getLines.toList
  var stlElements: List[STLElement] = List()

  def getSTLElements(conf: Config): List[STLElement] = {
    firstSPFileContent.zipWithIndex.filter {
      case (line, _) => line.matches("""^.[0-9]+_STL_[0-9]+.*""")
    }.map {
      case (line, i) => {
        line(0) match {
          case 'W' => STLWElement(line, i, conf)
          case _ => throw new RuntimeException("Invalid STL element is found.")
        }
      }
    }
  }

  def setSTLElements(elements: List[STLElement]): Unit = {
    stlElements = elements
  }
}
