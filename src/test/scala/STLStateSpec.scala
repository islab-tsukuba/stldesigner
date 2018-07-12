import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.Source

class STLStateSpec extends FlatSpec with MockFactory {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val server = stub[MockableSPServer]
  (server.runSpiceFile _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = new Config
  val state =
    STLState(SPFile("./src/test/resources/template/template_W.sp", conf), conf)
  val newSpFile = Source.fromFile("./src/test/resources/template/template_W_separated.sp")

  class MockableSPServer extends HspiceServer(cmdr, new Config())

  "calcScore()" should "return score of first state." in {
    assert(state.calcScore(server) === 1)
  }


  "createNeighbour()" should "return STLState which has shifted segments." in {
    val newState = state.createNeighbour()
    assert(newState.spFile.getString().split("\n").length == newSpFile.getLines().length)
  }
}
