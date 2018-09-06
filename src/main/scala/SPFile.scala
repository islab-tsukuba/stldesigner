import java.io.{File, PrintWriter}
import java.math.BigInteger
import java.security.MessageDigest

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

  def deepCopy(): SPFile = {
    val copy = this.copy()
    copy.setSTLElements(stlElements.map(stlElement => stlElement.deepCopy()))
    copy
  }

  def setSTLElements(elements: List[STLElement]): Unit = {
    stlElements = elements
  }

  def getSTLElements(): List[STLElement] = stlElements

  def writeToFile(file: File): Unit = {
    val writer = new PrintWriter(file)
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

  def md5Hash(): String = {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(getString().getBytes)
    val bigInt = new BigInteger(1, digest)
    val hashedString = bigInt.toString(16)
    hashedString
  }
}
