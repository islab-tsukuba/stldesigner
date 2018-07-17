import scala.collection.mutable
import scala.util.Random

trait Element {
  def getString(): String

  def shift(): Element
}

case class WElement(name: String, nodes: Array[String], values: mutable.LinkedHashMap[String, String],
                    conf: Config) extends Element {
  override def getString(): String = {
    name + " " + nodes.mkString(" ") + " " +
      values.map {
        case (key, value) => key + "=" + value
      }.mkString(" ")
  }

  override def shift(): Element = {
    val lenStr = values.getOrElse("L", "0.0")
    val len = UnitUtil.strToDouble(lenStr).getOrElse(0.0)
    val rand1 = Random.nextDouble()
    val newLen = len +
      (if (rand1 < 1.0 / 3.0) conf.segmentLengthStep
      else if (rand1 < 2.0 / 3.0) -conf.segmentLengthStep
      else 0)

    val imp = values.getOrElse("RLGCMODEL", "Z50")
    val impIndex = conf.segmentImpList.indexOf(imp)
    val rand2 = Random.nextDouble()
    var newImpIndex = impIndex +
      (if (rand2 < 1.0 / 3.0) 1
      else if (rand2 < 2.0 / 3.0) -1
      else 0)
    if (newImpIndex >= conf.segmentImpList.length) newImpIndex = conf.segmentImpList.length - 1
    if (newImpIndex < 0) newImpIndex = 0
    val newImp = conf.segmentImpList(newImpIndex)

    val newElement = this.copy()
    newElement.values.put("L", UnitUtil.doubleToStr(newLen))
    newElement.values.put("RLGCMODEL", newImp)
    newElement
  }
}
