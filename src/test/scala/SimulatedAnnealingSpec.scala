import org.scalamock.scalatest.MockFactory
import org.scalatest._

class SimulatedAnnealingSpec extends FlatSpec with DiagrammedAssertions with MockFactory {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = new Config()
  val server = stub[MockableSPServer]
  val stlState = new STLStateMock()
  val sa = SimulatedAnnealing(stlState, 10, 0.1, 0.3, server, "test")

  class STLStateMock extends STLState(SPFile("./src/test/resources/template/template_W.sp", conf), conf) {
    override def calcScore(server: HspiceServer): Double = 1.0

    override def createNeighbour(): STLState = this
  }

  class MockableSPServer extends HspiceServer(cmdr, conf)

  "probability()" should "return one if e1 is grater than e2" in {
    assert(sa.calcProbability(1.0, 0.5, 0.5) === 1)
  }

  "probability()" should "return probability of the Simulated Annealing" in {
    assert(sa.calcProbability(0.5, 1.0, 0.5) === 0.20574066108381447)
    assert(sa.calcProbability(0.5, 1.0, 0.9) === 0.01884248395797795)
  }

  "run()" should "return optimized STLState" in {
    assert(sa.run().calcScore(server) === 1.0)
  }
}