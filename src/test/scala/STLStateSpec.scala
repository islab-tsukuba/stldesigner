import java.io.File
import java.nio.file.{Files, Paths}

import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.Source

class STLStateSpec extends FlatSpec with DiagrammedAssertions with MockFactory with PrivateMethodTester {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val server = stub[MockableSPServer]
  (server.runSpiceFile _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = new Config
  val state =
    STLState(SPFile("./src/test/resources/template/template_W.sp", conf), conf)
  val newSpFile = Source.fromFile("./src/test/resources/template/template_W_separated.sp")
  val hash = state.spFile.md5Hash

  class MockableSPServer extends HspiceServer(cmdr, new Config())


  "calcScore()" should "return score of first state." in {
    // Create dummy lisFile.
    Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
      Paths.get("/dev/shm/" + hash + ".lis"))
    assert(state.calcScore(server) === 0.8576430835973875)
  }

  "deleteFileByPrefix()" should "delete files by Prefix." in {
    // Create dummy lisFile.
    Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
      Paths.get("/dev/shm/" + hash + ".lis"))
    val deleteFileByPrefix: PrivateMethod[Unit] = PrivateMethod[Unit]('deleteFileByPrefix)
    state invokePrivate deleteFileByPrefix("/dev/shm/", hash)
    val spFile = new File("/dev/shm/" + hash + ".lis")
    assert(spFile.exists() === false)
    val lisFile = new File("/dev/shm/" + hash + ".sp")
    assert(lisFile.exists() === false)
  }

  "createNeighbour()" should "return STLState which has shifted segments." in {
    val newState = state.createNeighbour()
    val newFileLength = newSpFile.getLines().length
    assert(newState.spFile.getString().split("\n").length === newFileLength)
  }
}
