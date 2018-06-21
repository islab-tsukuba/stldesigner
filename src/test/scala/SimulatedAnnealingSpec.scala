import org.scalatest._

class SimulatedAnnealingSpec extends FlatSpec with DiagrammedAssertions {
  val sa = SimulatedAnnealing(STLState(Seq(11, 11, 11, 11, 11, 11, 11, 11, 11)), 1000, 0.1, 0.3)

  "probability()" should "return one if e1 is grater than e2" in {
    assert(sa.CalcProbability(1.0, 0.5, 0.5) === 1)
  }

  "probability()" should "return probability of the Simulated Annealing" in {
    assert(sa.CalcProbability(0.5, 1.0, 0.5) === 0.20574066108381447)
    assert(sa.CalcProbability(0.5, 1.0, 0.9) === 0.01884248395797795)
  }

  "run()" should "return optimized STLState" in {
    assert(sa.run().getScore < 0.4)
  }
}