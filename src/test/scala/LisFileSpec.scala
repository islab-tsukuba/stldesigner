import org.scalatest._

class LisFileSpec extends FlatSpec with DiagrammedAssertions {
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", Config())
  "getDataLines" should "return all voltage data." in {
    assert(lisFile.getDataLines().length === 8003)
  }

  "getVoltage" should "return voltage list of single point." in {
    val voltageList = lisFile.getVoltage("optpt3")
    assert(voltageList.length === 8001)
    assert(voltageList.head === 0.0)
  }
}
