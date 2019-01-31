package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.ConfigBuilder
import org.scalatest._

class LisFileTest extends FlatSpec with DiagrammedAssertions {
  val conf = ConfigBuilder().getFromYAML("./src/test/resources/config/test.yml")
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", ConfigBuilder().getDefaultConfig())
  "getDataLines" should "return all voltage data." in {
    assert(lisFile.getDataLines().length === 8003)
  }

  "getVoltage" should "return voltage list of single point." in {
    val voltageList = lisFile.getVoltage("optpt3")
    assert(voltageList.length === 8001)
    assert(voltageList.head === 0.0)
  }
}
