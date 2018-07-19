import java.io.File

import scala.util.Random

class SimulatedAnnealing(firstState: STLState, maxItr: Int, targetTemp: Double, goalScore: Double,
                         server: HspiceServer, id: String) {
  Random.setSeed(1)

  def run(): STLState = {
    var state = firstState.createRandom()
    var score = state.calcScore(server)
    var bestState = state
    var bestScore = score
    for (i <- 0 until maxItr) {
      val nextState = state.createNeighbour()
      val nextScore = nextState.calcScore(server)
      if (nextScore < bestScore) {
        bestState = nextState
        bestScore = nextScore
        val dirPath = "./output/" + id + "/"
        val dir = new File(dirPath)
        if (!dir.exists()) dir.mkdir()
        bestState.spFile.writeToFile(dirPath + "gen" + i + ".sp")
        if (bestScore < goalScore) return bestState
      }
      println("Probability: " + calcProbability(score, nextScore, i.toDouble / maxItr.toDouble))
      if (Random.nextDouble() <= calcProbability(score, nextScore, i.toDouble / maxItr.toDouble)) {
        state = nextState
        score = nextScore
      }
      println("Gen: " + i + ", Score: " + nextScore)
    }
    println("Best score: " + bestScore)
    bestState
  }

  def calcProbability(e1: Double, e2: Double, progress: Double): Double = {
    if (e1 >= e2) 1
    else {
      val temperature = Math.pow(targetTemp, progress)
      Math.pow(Math.E, (e1 - e2) / temperature)
    }
  }
}

object SimulatedAnnealing {
  def apply(firstState: STLState, maxItr: Int, alpha: Double, goalE: Double,
            server: HspiceServer, id: String):
  SimulatedAnnealing = new SimulatedAnnealing(firstState, maxItr, alpha, goalE, server, id)
}