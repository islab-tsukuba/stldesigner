package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalatest._

class LisFileTest extends FlatSpec with DiagrammedAssertions {
  val conf = ConfigReader().getFromYAML(getClass().getResource("/config/test_sa.yml").getPath)
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", conf)
  "getDataLines" should "return all voltage data." in {
    assert(lisFile.getDataLines().length === 8003)
  }

  "getVoltage" should "return voltage list of single point." in {
    val voltageList = lisFile.getVoltage("optpt3")
    assert(voltageList.length === 8001)
    assert(voltageList.head === 0.0)
  }
}
