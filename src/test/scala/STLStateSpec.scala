import org.scalatest._

class STLStateSpec extends FlatSpec with DiagrammedAssertions {
  val state = STLState(Seq(11, 11, 11, 11, 11, 11, 11, 11, 11))

  "calcScore()" should "return sum of Seq" in {
    assert(state.calcScore === 11.0 / 19.0)
  }

  "createNeighbour()" should "return STLState which has shifted segments." in {
    val segments = state.segments
    val nextSegments = state.createNeighbour().segments
    for (i <- segments.indices)
      assert(segments(i) >= nextSegments(i) - 1 && segments(i) <= nextSegments(i) + 1)
  }
}
