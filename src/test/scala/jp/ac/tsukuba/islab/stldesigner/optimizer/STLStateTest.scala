package jp.ac.tsukuba.islab.stldesigner.optimizer

import java.io.File
import java.nio.file.{Files, Paths}

import jp.ac.tsukuba.islab.stldesigner.circuit.{CommandRunner, ExecResult, HspiceServer, SPFile}
import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.Source

class STLStateTest extends FlatSpec with DiagrammedAssertions with MockFactory with PrivateMethodTester {
  val cmdr = stub[CommandRunner]
  (cmdr.runCommand _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val conf = ConfigReader().getFromYAML(getClass().getResource("/config/test_sa.yml").getPath)
  val server = stub[MockableSPServer]
  (server.runSpiceFile _).when(*).returns(ExecResult(0, Seq(), Seq()))
  val state = STLState(SPFile(conf), conf, server, 0, 1.7699115044247788)
  val newSpFile = Source.fromFile("./src/test/resources/template/template_W_separated.sp")
  val hash = state.spFile.md5Hash

  class MockableSPServer extends HspiceServer(cmdr, conf)


  "calcScore()" should "return score of first state." in {
    // Create dummy lisFile.
    Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
      Paths.get("/dev/shm/test.lis"))
    val firstScore = state.calcFirstScore("test")
    assert(firstScore === 1.7699115044247788)
    // Create dummy lisFile.
    Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
      Paths.get("/dev/shm/" + hash + "_" + 0 + ".lis"))
    val score = state.calcScore()
    assert(score === 1.0)
  }

  "deleteFileByPrefix()" should "delete files by Prefix." in {
    // Create dummy lisFile.
    Files.copy(Paths.get("./src/test/resources/output/template_W.lis"),
      Paths.get("/dev/shm/" + hash + "_" + 0 + ".lis"))
    state.deleteFileByPrefix("/dev/shm/", hash)
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

  "createShiftSegmentList()" should "have list of segment shifted by createNeighbor function." in {
    val createShiftSegmentList: PrivateMethod[Seq[Boolean]] = PrivateMethod[Seq[Boolean]]('createShiftSegmentList)
    val seq = state invokePrivate createShiftSegmentList(10, 3)
    assert(seq.length === 10)
    assert(seq.count(_ == true) === 3)
  }
}
