package jp.ac.tsukuba.islab.stldesigner.evaluator

import jp.ac.tsukuba.islab.stldesigner.spice.{LisFile, Tran}
import jp.ac.tsukuba.islab.stldesigner.util.ConfigBuilder
import org.scalatest._

class EyeSizeEvaluatorTest extends FlatSpec with DiagrammedAssertions with PrivateMethodTester {
  val defaultConfig = ConfigBuilder().getFromYAML("./src/test/resources/config/test.yml")
  val lisFile = new LisFile(
    "./src/test/resources/output/template_W.lis", defaultConfig)
  val eyeSizeEvaluator = new EyeSizeEvaluator(lisFile, defaultConfig, Tran(".TRAN 5p 100n 0n"))

  "evaluate()" should "return signal score." in {
    assert(eyeSizeEvaluator.evaluate() === 1.7699115044247788)
  }

  "calcSinglePoint()" should "return single point signal score." in {
    val calcSinglePoint: PrivateMethod[Double] = PrivateMethod[Double]('calcSinglePoint)
    val score = eyeSizeEvaluator invokePrivate calcSinglePoint(lisFile.getVoltage("optpt3"))
    assert(score === 1.7699115044247788)
  }

  "calcSinglePoint()" should "return four eye lines." in {
    val getEyeLines: PrivateMethod[Seq[Seq[Double]]] = PrivateMethod[Seq[Seq[Double]]]('getEyeLines)
    val eyeDiagram = eyeSizeEvaluator invokePrivate getEyeLines(lisFile.getVoltage("optpt3"))
    assert(eyeDiagram.size === 4)
  }
}
