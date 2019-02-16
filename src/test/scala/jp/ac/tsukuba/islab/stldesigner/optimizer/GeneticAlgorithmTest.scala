package jp.ac.tsukuba.islab.stldesigner.optimizer

import java.nio.file.{Files, Paths}

import jp.ac.tsukuba.islab.stldesigner.circuit.{CommandRunner, ExecResult, HspiceServer, SPFile}
import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class GeneticAlgorithmTest extends FlatSpec with DiagrammedAssertions with MockFactory {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = ConfigReader().getFromYAML(getClass().getResource("/config/test_ga.yml").getPath)
  conf.gaConf.maxItr = 10
  val server = stub[MockableSPServer]
  // Create dummy lisFile.
  Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
    Paths.get("/dev/shm/first-ga.lis"))
  val stlState = new STLStateMock()
  val ga = GeneticAlgorithm(stlState, conf)

  class STLStateMock extends STLState(SPFile(conf), conf, server, 0) {
    override def calcScore(): Double = 1.0

    override def calcFirstScore(outputName: String = "first-ga"): Double = 1.0

    override def createNeighbour(): STLState = this

    override def createRandom(): STLState = this
  }

  class MockableSPServer extends HspiceServer(cmdr, conf)

  "run()" should "return optimized optimizer.STLState" in {
    assert(ga.run().calcScore() === 1.0)
  }
}