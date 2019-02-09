package jp.ac.tsukuba.islab.stldesigner.optimizer

import java.nio.file.{Files, Paths}

import jp.ac.tsukuba.islab.stldesigner.circuit.{CommandRunner, ExecResult, HspiceServer, SPFile}
import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class SimulatedAnnealingTest extends FlatSpec with DiagrammedAssertions with MockFactory {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = ConfigReader().getFromYAML(getClass().getResource("/config/test_sa.yml").getPath)
  conf.saConf.maxItr = 10
  val server = stub[MockableSPServer]
  // Create dummy lisFile.
  Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
    Paths.get("/dev/shm/first.lis"))
  val sa = SimulatedAnnealing(server, conf)
  val stlState = new STLStateMock()
  sa.firstState = stlState

  class STLStateMock extends STLState(SPFile(conf), conf, 0) {
    override def calcScore(server: HspiceServer): Double = 1.0

    override def calcFirstScore(server: HspiceServer, outputName: String = "first"): Double = 1.0

    override def createNeighbour(): STLState = this

    override def createRandom(): STLState = this
  }

  class MockableSPServer extends HspiceServer(cmdr, conf)

  "run()" should "return optimized optimizer.STLState" in {
    assert(sa.run().calcScore(server) === 1.0)
  }
}