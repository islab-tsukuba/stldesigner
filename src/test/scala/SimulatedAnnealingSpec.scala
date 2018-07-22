import org.scalamock.scalatest.MockFactory
import org.scalatest._

class SimulatedAnnealingSpec extends FlatSpec with DiagrammedAssertions with MockFactory {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = new Config()
  conf.saConf.maxItr = 10
  val server = stub[MockableSPServer]
  val stlState = new STLStateMock()
  val sa = SimulatedAnnealing(stlState, server, conf)

  class STLStateMock extends STLState(SPFile("./src/test/resources/template/template_W.sp", conf), conf, 0) {
    override def calcScore(server: HspiceServer): Double = 1.0

    override def createNeighbour(): STLState = this

    override def createRandom(): STLState = this
  }

  class MockableSPServer extends HspiceServer(cmdr, conf)

  "run()" should "return optimized STLState" in {
    assert(sa.run().calcScore(server) === 1.0)
  }
}