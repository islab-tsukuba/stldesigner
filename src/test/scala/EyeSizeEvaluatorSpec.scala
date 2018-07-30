import org.scalatest._

class EyeSizeEvaluatorSpec extends FlatSpec with DiagrammedAssertions with PrivateMethodTester {
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", new Config())
  val eyeSizeEvaluator = new EyeSizeEvaluator(new Config(), Tran(".TRAN 5p 100n 0n"))

  "evaluate()" should "return signal score." in {
    assert(eyeSizeEvaluator.evaluate(lisFile) === 2.414282538376363)
  }

  "calcSinglePoint()" should "return single point signal score." in {
    val calcSinglePoint: PrivateMethod[Double] = PrivateMethod[Double]('calcSinglePoint)
    val score = eyeSizeEvaluator invokePrivate calcSinglePoint(lisFile.getVoltage("optpt3"))
    assert(score === 2.414282538376363)
  }
}
