package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalatest._

import scala.collection.mutable

class ElementTest extends FlatSpec with DiagrammedAssertions {
  val conf = ConfigReader().getFromYAML(getClass().getResource("/config/test_sa.yml").getPath)
  val element = WElement(
    "W1_SEG_1",
    Array("102", "0", "optpt1", "0"),
    mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "100m"), conf)

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
}
