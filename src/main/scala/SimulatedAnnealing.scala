import scala.util.Random

class SimulatedAnnealing(startState: STLState, maxItr: Int, alpha: Double, goalScore: Double,
                         server: HspiceServer) {
  Random.setSeed(1)

  def run(): STLState = {
    var state = startState
    var score = startState.calcScore(server)
    var bestState = state
    var bestScore = score
    for (i <- 0 until maxItr) {
      val nextState = state.createNeighbour()
      val nextScore = nextState.calcScore(server)
      if (nextScore < bestScore) {
        bestState = nextState
        bestScore = nextScore
        if (bestScore < goalScore) return bestState
      }
      if (Random.nextDouble() <= calcProbability(score, nextScore, i / maxItr)) {
        state = nextState
        score = nextScore
      }
    }
    bestState
  }

  def calcProbability(e1: Double, e2: Double, progress: Double): Double = {
    if (e1 >= e2) 1
    else {
      val temperature = Math.pow(alpha, progress)
      Math.pow(Math.E, (e1 - e2) / temperature)
    }
  }
}

object SimulatedAnnealing {
  def apply(startState: STLState, maxItr: Int, alpha: Double, goalE: Double, server: HspiceServer):
  SimulatedAnnealing = new SimulatedAnnealing(startState, maxItr, alpha, goalE, server)
}