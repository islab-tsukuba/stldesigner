import org.scalamock.scalatest.MockFactory
import org.scalatest.{DiagrammedAssertions, FlatSpec}

class SAStateTest extends FlatSpec with DiagrammedAssertions with MockFactory {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = new Config()
  val server = stub[MockableSPServer]
  val saState = new SAState(new STLStateMock(), server, conf, "test")

  class STLStateMock extends STLState(SPFile("./src/test/resources/template/template_W.sp", conf), conf) {
    override def calcScore(server: HspiceServer): Double = 1.0

    override def createNeighbour(): STLState = this

    override def createRandom(): STLState = this
  }

  class MockableSPServer extends HspiceServer(cmdr, conf)

  "moveToNextState()" should "change state." in {
    saState.moveToNextState()
    assert(saState.generation === 2)
  }

  "probability()" should "return one if e1 is grater than e2" in {
    assert(saState.calcProbability(1.0, 0.5, 0.5) === 1)
  }

  "probability()" should "return probability of the Simulated Annealing" in {
    assert(saState.calcProbability(0.5, 1.0, 0.5) === 0.006737946999085469)
  }
}
