package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.{Config, UnitUtil}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.Random

trait Element {
  def getString(): String

  def shift(): Element

  def cross(element: Element): Element

  def random(): Element

  def getLength(): Double

  def setLength(len: Double): Element
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
    val rand1 = Random.nextDouble()
    val newLen = getLength() +
      (if (rand1 < 1.0 / 3.0) conf.segmentLengthStep
      else if (rand1 < 2.0 / 3.0) -conf.segmentLengthStep
      else 0)

    val imp = getImpedance()
    val impIndex = conf.segmentImpList.indexOf(imp)
    val rand2 = Random.nextDouble()
    var newImpIndex = impIndex +
      (if (rand2 < 1.0 / 3.0) 1
      else if (rand2 < 2.0 / 3.0) -1
      else 0)
    val impLength = conf.segmentImpList.size
    if (newImpIndex >= impLength) newImpIndex = impLength - 1
    if (newImpIndex < 0) newImpIndex = 0
    val newImp = conf.segmentImpList.asScala(newImpIndex)

    val newElement = this.copy(values = values.clone())
    newElement.setLength(newLen)
    newElement.setImpedance(newImp)
    newElement
  }

  override def getLength(): Double = {
    val lenStr = values.getOrElse("L", "0.0")
    UnitUtil.strToDouble(lenStr).getOrElse(0.0)
  }

  override def setLength(len: Double): Element = {
    values.put("L", UnitUtil.doubleToStr(len))
    this
  }

  override def cross(element: Element): Element = {
    if (!element.isInstanceOf[WElement]) {
      throw new RuntimeException("Input element is not WElement.")
    }
    val wElement = element.asInstanceOf[WElement]
    val len1 = this.getLength()
    val len2 = wElement.getLength()
    val newLen = blxAlpha(len1, len2)
    val impIndex1 = conf.segmentImpList.indexOf(this.getImpedance())
    val impIndex2 = conf.segmentImpList.indexOf(wElement.getImpedance())
    var newImpIndex = math.round(blxAlpha(impIndex1, impIndex2)).toInt
    if (newImpIndex < 0) newImpIndex = 0
    if (newImpIndex >= conf.segmentImpList.size()) newImpIndex = conf.segmentImpList.size() - 1
    val newImp = conf.segmentImpList.asScala(newImpIndex)
    val newElement = this.copy(values = values.clone())
    newElement.setLength(newLen)
    newElement.setImpedance(newImp)
    newElement
  }

  private def blxAlpha(p1 : Double, p2 : Double): Double = {
    val delta = math.abs(p1 - p2)
    val rangeMin = math.min(p1, p2) - delta * conf.gaConf.blxAlpha
    val rangeMax = math.max(p1, p2) + delta * conf.gaConf.blxAlpha
    rangeMin + (rangeMax - rangeMin) * Random.nextDouble()
  }

  def getImpedance(): String = {
    values.getOrElse("RLGCMODEL", "Z50")
  }

  def setImpedance(imp: String): Unit = {
    values.put("RLGCMODEL", imp)
  }

  override def random(): Element = {
    val newLen = Random.nextDouble()
    val newImpIndex = Random.nextInt(conf.segmentImpList.size)
    val newImp = conf.segmentImpList.asScala(newImpIndex)
    val newElement = this.copy(values = values.clone())
    newElement.setLength(newLen)
    newElement.setImpedance(newImp)
    newElement
  }
}
