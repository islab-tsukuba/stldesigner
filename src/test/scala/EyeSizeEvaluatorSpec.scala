import org.scalatest._

class EyeSizeEvaluatorSpec extends FlatSpec with DiagrammedAssertions with PrivateMethodTester {
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", new Config())
  val eyeSizeEvaluator = new EyeSizeEvaluator(lisFile, new Config(), Tran(".TRAN 5p 100n 0n"))

  "evaluate()" should "return signal score." in {
    assert(eyeSizeEvaluator.evaluate() === 2.414282538376363)
  }

  "calcSinglePoint()" should "return single point signal score." in {
    val calcSinglePoint: PrivateMethod[Double] = PrivateMethod[Double]('calcSinglePoint)
    val score = eyeSizeEvaluator invokePrivate calcSinglePoint(lisFile.getVoltage("optpt3"))
    assert(score === 2.414282538376363)
  }

  "calcSinglePoint()" should "return four eye lines." in {
    val getEyeLines: PrivateMethod[Seq[Seq[Double]]] = PrivateMethod[Seq[Seq[Double]]]('getEyeLines)
    val eyeDiagram = eyeSizeEvaluator invokePrivate getEyeLines(lisFile.getVoltage("optpt3"))
    assert(eyeDiagram.size === 4)
  }
}
