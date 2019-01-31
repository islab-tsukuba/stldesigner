package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.{Config, UnitUtil}

import scala.collection.mutable

case class STLElement(line: String, index: Int, conf: Config) {
  val splitLine: Array[String] = line.split("""\s+""")
  val name: String = splitLine(0)
  val nameIndex: Int = """^.(\d)_.*""".r.findAllIn(name).group(1).toInt
  val sepNum: Int = name.split("_").last.toInt
  val params: Array[String] = splitLine.drop(1)
  val nodes: Array[String] = params.take(4)
  // Use LinkedHashMap to keep order of values.
  val values: mutable.LinkedHashMap[String, String] = createValueMap()
  val lenStr = this.values.getOrElse("L", throw new RuntimeException("Length is not defined."))
  val totalLen = UnitUtil.strToDouble(lenStr).getOrElse(0.0)
  var elements: Seq[Element] = createElements()

  def deepCopy(): STLElement = {
    val copy = this.copy()
    copy.elements = elements.map(element => element.deepCopy())
    copy
  }

  def shiftElements(shiftList: Seq[Boolean]): STLElement = {
    elements =
      for (i <- shiftList.indices) yield {
        var element: Element = null
        if (shiftList(i)) {
          element = elements(i).shift()
        } else {
          element = elements(i)
        }
        element
    }
    adjustLength()
    this
  }

  private def adjustLength(): STLElement = {
    val currentTotalLen = elements.map(_.getLength()).sum
    val ratio = totalLen / currentTotalLen
    elements.map(element => element.setLength(element.getLength() * ratio))
    this
  }

  def assignRandom(): STLElement = {
    elements = this.elements.map(element => element.random())
    adjustLength()
    this
  }

  def getElementLines(): List[String] = {
    elements.map(element => element.getString()).toList
  }

  def getElements(): Seq[Element] = elements

  private def createElements(): Seq[WElement] = {
    var lenSum = 0.0
    for (i <- 0 until sepNum) yield {
      val name = this.name.split("_")(0) + "_SEG_" + (i + 1).toString
      val node_start = if (i == 0) this.nodes(0) else 1000 * nameIndex * 2 + i - 1
      val node_end = if (i == sepNum - 1) this.nodes(2) else 1000 * nameIndex * 2 + i
      val nodes = Array(node_start.toString, this.nodes(1), node_end.toString, this.nodes(3))
      var len = totalLen / sepNum
      lenSum += len
      // Adjust total length.
      if (i == sepNum - 1) len = len + totalLen - lenSum
      val values = this.values.clone()
      values.put("L", UnitUtil.doubleToStr(len))
      WElement(name, nodes, values, conf)
    }
  }

  private def createValueMap(): mutable.LinkedHashMap[String, String] = {
    val values: mutable.LinkedHashMap[String, String] = mutable.LinkedHashMap()
    params.drop(4)
      .map(str => {
        val v = str.split("=")
        values += (v(0) -> v(1))
      })
    values
  }
}
