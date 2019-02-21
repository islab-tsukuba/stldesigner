package jp.ac.tsukuba.islab.stldesigner.optimizer

import jp.ac.tsukuba.islab.stldesigner.circuit.{CommandRunner, ExecResult, HspiceServer, SPFile}
import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class GeneticAlgorithmTest extends FlatSpec with DiagrammedAssertions with MockFactory with PrivateMethodTester {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = ConfigReader().getFromYAML(getClass().getResource("/config/test_ga.yml").getPath)
  conf.gaConf.maxItr = 10
  val server = stub[MockableSPServer]
  val stlState = new STLStateMock()
  val ga = GeneticAlgorithm(stlState, conf)

  class STLStateMock extends STLState(SPFile(conf), conf, server, 0) {
    override def calcScore(): Double = 1.0

    override def calcFirstScore(outputName: String = "first"): Double = 1.0

    override def createNeighbour(): STLState = this

    override def createCross(state: STLState): STLState = this

    override def createRandom(): STLState = this
  }

  class MockableSPServer extends HspiceServer(cmdr, conf)

  "run()" should "return optimized optimizer.STLState" in {
    assert(ga.run().calcScore() === 1.0)
  }

  "rouletteChoice()" should "return some STLState." in {
    val states = for (score <- Seq(1.0, 0.8, 0.7, 0.3))
      yield {
        val state = new STLStateMock()
        state.score = score
        state
      }
    val rouletteChoice: PrivateMethod[Seq[STLState]] =
      PrivateMethod[Seq[STLState]]('rouletteChoice)
    for(_ <- 0 until 100) {
      val chosenStates = ga invokePrivate rouletteChoice(states, 2)
      assert(chosenStates.size == 2)
      assert(chosenStates.head.score != chosenStates(1).score)
    }
  }
}