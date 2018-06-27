import org.scalatest._

class STLStateSpec extends FlatSpec with DiagrammedAssertions with Matchers {
  val state = STLState(SPFile("./src/test/resources/template/template_W.sp"), new Config)

  "calcScore()" should "return score of first state." in {
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
