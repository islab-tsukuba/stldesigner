import org.scalatest._

class LisFileSpec extends FlatSpec with DiagrammedAssertions with Matchers {
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", new Config(), Tran(".TRAN 10p 24n 20n"))
  "getDataLines" should "return all voltage data." in {
    assert(lisFile.getDataLines().length === 403)
  }

  "getVoltage" should "return voltage list of single point." in {
    val voltageList = lisFile.getVoltage("optpt3")
    assert(voltageList.length === 401)
    assert(voltageList.head === (0.1158813 +- 1e-8))
  }
}
