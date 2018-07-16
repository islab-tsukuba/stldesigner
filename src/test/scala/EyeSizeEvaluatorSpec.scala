import org.scalatest._

class EyeSizeEvaluatorSpec extends FlatSpec with DiagrammedAssertions with PrivateMethodTester {
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", new Config())
  val eyeSizeEvaluator = new EyeSizeEvaluator(new Config(), Tran(".TRAN 10p 24n 20n"))

  "evaluate()" should "return signal score." in {
    assert(eyeSizeEvaluator.evaluate(lisFile) === 0.8576430835973875)
  }

  "calcSinglePoint()" should "return single point signal score." in {
    val calcSinglePoint: PrivateMethod[Double] = PrivateMethod[Double]('calcSinglePoint)
    val score = eyeSizeEvaluator invokePrivate calcSinglePoint(lisFile.getVoltage("optpt3"))
    assert(score === 0.8576430835973875)
  }
}
