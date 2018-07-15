import java.io.{File, PrintWriter}

import scala.io.Source

case class SPFile(path: String, config: Config) {
  val firstSPFileContent: List[String] = Source.fromFile(path).getLines.toList
  var stlElements: List[STLElement] = getFirstSTLElements()

  def getFirstSTLElements(): List[STLElement] = {
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

  def getSTLElements(): List[STLElement] = stlElements

  def setSTLElements(elements: List[STLElement]): Unit = {
    stlElements = elements
  }

  def writeToFile(path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    writer.write(getString())
    writer.close()
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

  def getTran(): Tran = {
    val tranLine = firstSPFileContent.filter(line => line.matches("""^\.TRAN.*""")).head
    Tran(tranLine)
  }
}
