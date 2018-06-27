import org.scalatest._

import scala.io.Source

class STLStateSpec extends FlatSpec with DiagrammedAssertions with Matchers {
  val state = STLState(SPFile("./src/test/resources/template/template_W.sp"), new Config)

  "createStateFromSPFile()" should "create default status from spice file." in {
    state.setStateFromSPFile("./src/test/resources/template/template_W.sp")
    val content = Source.fromFile("./src/test/resources/template/template_W.sp").getLines.mkString("\n")
    assert(state.firstSPFileContent === content)
    assert(state.subSpaces ===
      Seq(
        SubSpace(Seq(
          Segment(0.02, 50),
          Segment(0.02, 50),
          Segment(0.02, 50),
          Segment(0.02, 50),
          Segment(0.02, 50)), 0.1),
        SubSpace(Seq(
          Segment(0.07, 50),
          Segment(0.07, 50),
          Segment(0.07, 50),
          Segment(0.07, 50),
          Segment(0.07, 50)), 0.35),
        SubSpace(Seq(
          Segment(0.03, 50),
          Segment(0.03, 50),
          Segment(0.03, 50),
          Segment(0.03, 50),
          Segment(0.03, 50)), 0.15))
    )
  }

  "calcScore()" should "return sum of Seq" in {
    assert(state.calcScore === 1)
  }

  "createNeighbour()" should "return STLState which has shifted segments." in {
    val sub = state.subSpaces
    val nextSub = state.createNeighbour().subSpaces
    for (i <- sub.indices) {
      val seg = sub(i).segments
      val nextSeg = nextSub(i).segments
      for (j <- seg.indices) {
        assert(seg(j).impedance >= nextSeg(j).impedance - 10 &&
          seg(j).impedance <= nextSeg(j).impedance + 10)
      }
      var lenSum: Double = 0
      for (nSeg <- nextSeg) {
        lenSum += nSeg.length
      }
      (lenSum) should be(nextSub(i).totalLength +- .000001)
    }
  }
}
