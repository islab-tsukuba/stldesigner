package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalatest._

import scala.collection.mutable

class WElementTest extends FlatSpec with DiagrammedAssertions with PrivateMethodTester {
  val confSA = ConfigReader().getFromYAML(getClass().getResource("/config/test_sa.yml").getPath)
  val confGA = ConfigReader().getFromYAML(getClass().getResource("/config/test_ga.yml").getPath)
  val element = WElement(
    "W1_SEG_1",
    Array("102", "0", "optpt1", "0"),
    mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "100m"), confSA)

  "WElement.getString()" should "return spice line." in {
    assert(element.getString() === "W1_SEG_1 102 0 optpt1 0 RLGCMODEL=Z50 N=1 L=100m")
  }

  "WElement.shift()" should "return shifted element." in {
    assert(element.shift().getString() != "W1_SEG_1 102 0 optpt1 0 RLGCMODEL=Z50 N=1 L=100m")
  }

  "WElement.random()" should "return random element." in {
    assert(element.random().getString().matches("""W1_SEG_1 102 0 optpt1 0 RLGCMODEL=Z\d+ N=1 L=\d+\.?\d*m"""))
  }

  "WElement.setLength()" should "sets element length." in {
    element.setLength(0.15)
    assert(element.getString().matches("""W1_SEG_1 102 0 optpt1 0 RLGCMODEL=Z\d+ N=1 L=150.000000m"""))
  }

  "WElement.setImpedance()" should "sets element impedance." in {
    element.setImpedance("Z100")
    assert(element.getString().matches("""W1_SEG_1 102 0 optpt1 0 RLGCMODEL=Z100 N=1 L=150.000000m"""))
  }

  "WElement.getLength()" should "return element length." in {
    assert(element.getLength() === 0.15)
  }

  "WElement.getImpedance()" should "return element impedance." in {
    assert(element.getImpedance() === "Z100")
  }

  "WElement.blxAlpha()" should "return result of blxAlpha." in {
    val gaElement = WElement(
      "W1_SEG_1",
      Array("102", "0", "optpt1", "0"),
      mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "100m"), confGA)
    val blxAlpha: PrivateMethod[Double] = PrivateMethod[Double]('blxAlpha)
    val result = gaElement invokePrivate blxAlpha(1.0, 2.0)
    val alpha = confGA.gaConf.blxAlpha
    assert(result >= 1.0 - 1.0 * alpha && result <= 2.0 + 1.0 * alpha)
  }

  "WElement.cross()" should "return crossed element." in {
    val elementForCross0 = WElement(
      "W1_SEG_1",
      Array("102", "0", "optpt1", "0"),
      mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "100m"), confGA)
    val elementForCross1 = WElement(
      "W1_SEG_1",
      Array("102", "0", "optpt1", "0"),
      mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "100m"), confGA)
    val elementForCross2 = WElement(
      "W1_SEG_1",
      Array("102", "0", "optpt1", "0"),
      mutable.LinkedHashMap("RLGCMODEL" -> "Z55", "N" -> "1", "L" -> "110m"), confGA)
    val crossedElement1 = elementForCross0.asInstanceOf[WElement].cross(elementForCross1).asInstanceOf[WElement]
    val crossedElement2 = elementForCross0.asInstanceOf[WElement].cross(elementForCross2).asInstanceOf[WElement]
    val alpha = confGA.gaConf.blxAlpha
    assert(crossedElement1.getImpedance() === "Z50")
    assert(crossedElement1.getLength() >= 0.1 * (1 - alpha) && crossedElement1.getLength() <= 0.1 * (1 + alpha))
    assert(crossedElement2.getImpedance() === "Z50" || crossedElement2.getImpedance() === "Z55")
    assert(crossedElement2.getLength() >= 0.1 * (1 - alpha) && crossedElement1.getLength() <= 0.11 * (1 + alpha))
  }
}
