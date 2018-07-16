import java.nio.file.{Files, Paths}

import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.Source

class STLStateSpec extends FlatSpec with DiagrammedAssertions with MockFactory {
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
    // Create dummy lisFile.
    val hash = math.abs(state.spFile.getSTLElements().hashCode())
    Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
      Paths.get("/dev/shm/" + hash + ".lis"))
    assert(state.calcScore(server) === 1.0)
  }

  "createNeighbour()" should "return STLState which has shifted segments." in {
    val newState = state.createNeighbour()
    val newFileLength = newSpFile.getLines().length
    assert(newState.spFile.getString().split("\n").length === newFileLength)
  }
}
