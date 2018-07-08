import org.scalatest._

import scala.io.Source

class STLStateSpec extends FlatSpec {
  val state =
    STLState(SPFile("./src/test/resources/template/template_W.sp"), new Config)

  "calcScore()" should "return score of first state." in {
    assert(state.calcScore(new CommandRunner()) === 1)
  }

  val newSpFile = Source.fromFile("./src/test/resources/template/template_W_separated.sp")
  "createNeighbour()" should "return STLState which has shifted segments." in {
    val newState = state.createNeighbour()
    assert(newState.spFile.getString().split("\n").length == newSpFile.getLines().length)
  }
}
