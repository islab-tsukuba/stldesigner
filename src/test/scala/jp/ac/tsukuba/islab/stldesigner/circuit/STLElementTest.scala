package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.ConfigBuilder
import org.scalatest._

import scala.collection.mutable

class STLElementTest extends FlatSpec with DiagrammedAssertions with Matchers with PrivateMethodTester {
  val conf = ConfigBuilder().getFromYAML("./src/test/resources/config/test.yml")
  val stlElement = STLElement(
    "W1_STL_5        102     0       optpt1  0       RLGCMODEL=Z50   N=1     L=100m",
    0, conf)

  "STLWElement()" should "read parameters." in {
    assert(stlElement.name === "W1_STL_5")
    assert(stlElement.nameIndex === 1)
    assert(stlElement.sepNum === 5)
    assert(stlElement.params === Array("102", "0", "optpt1", "0", "RLGCMODEL=Z50", "N=1", "L=100m"))
    assert(stlElement.nodes === Array("102", "0", "optpt1", "0"))
    assert(stlElement.values === Map("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "100m"))
  }

  "getElementLines()" should "return spice line of separated stlElement." in {
    assert(stlElement.getElementLines() === Seq(
      WElement(
        "W1_SEG_1",
        Array("102", "0", "2000", "0"),
        mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "20.000000m"),
        conf
      ).getString(),
      WElement(
        "W1_SEG_2",
        Array("2000", "0", "2001", "0"),
        mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "20.000000m"),
        conf
      ).getString(),
      WElement(
        "W1_SEG_3",
        Array("2001", "0", "2002", "0"),
        mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "20.000000m"),
        conf
      ).getString(),
      WElement(
        "W1_SEG_4",
        Array("2002", "0", "2003", "0"),
        mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "20.000000m"),
        conf
      ).getString(),
      WElement(
        "W1_SEG_5",
        Array("2003", "0", "optpt1", "0"),
        mutable.LinkedHashMap("RLGCMODEL" -> "Z50", "N" -> "1", "L" -> "20.000000m"),
        conf
      ).getString()
    ))
  }

  "shiftElements()" should "execute single element shift." in {
    val elements = stlElement.shiftElements(Seq(true, false, false, false, false)).getElements()
    val lengthList = elements.map(_.getLength())
    val subList = lengthList.slice(1, stlElement.sepNum)
    val nonShiftedLen = subList(0)
    assert(subList.count(_ == nonShiftedLen) === subList.length)
    assert(lengthList.sum === (0.1 +- 0.000001))
  }

  "assignRandom()" should "return same length as first element." in {
    val elements = stlElement.assignRandom().getElements()
    assert(elements.map(_.getLength()).sum === (0.1 +- 0.000001))
  }
}
