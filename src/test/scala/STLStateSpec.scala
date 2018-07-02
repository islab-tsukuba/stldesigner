import org.scalatest._

class STLStateSpec extends FlatSpec with DiagrammedAssertions with Matchers {
  val state =
    STLState(SPFile("./src/test/resources/template/template_W.sp"), new Config)

  "calcScore()" should "return score of first state." in {
    assert(state.calcScore === 1)
  }

  "createNeighbour()" should "return STLState which has shifted segments." in {
    val newState = state.createNeighbour()
    assert(newState.stlElements.map(stlElement => stlElement.getElementLines()) !=
      state.stlElements.map(stlElement => stlElement.getElementLines()))
  }
}
