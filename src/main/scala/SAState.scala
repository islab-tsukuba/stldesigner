import java.io.File

import scala.util.Random

class SAState(firstState: STLState, server: HspiceServer, conf: Config, name: String) {
  var state = firstState
  var score = state.calcScore(server)
  var bestState = state
  var bestScore = score
  var generation = 1

  def moveToNextState(): Unit = {
    val nextState = state.createNeighbour()
    val nextScore = nextState.calcScore(server)
    if (nextScore < bestScore) {
      bestState = nextState
      bestScore = nextScore
      val dirPath = "./output/" + name + "/"
      val dir = new File(dirPath)
      if (!dir.exists()) dir.mkdir()
      bestState.spFile.writeToFile(dirPath + "gen" + generation + ".sp")
    }
    val prob = calcProbability(score, nextScore, generation.toDouble / conf.saConf.maxItr.toDouble)
    println("Probability: " + prob)
    if (Random.nextDouble() <= prob) {
      state = nextState
      score = nextScore
    }
    println("Gen: " + generation + ", Score: " + nextScore)
    generation += 1
  }

  def calcProbability(e1: Double, e2: Double, progress: Double): Double = {
    if (e1 >= e2) 1
    else {
      val temperature = Math.pow(conf.saConf.targetTemp, progress)
      Math.pow(Math.E, (e1 - e2) / temperature)
    }
  }
}
