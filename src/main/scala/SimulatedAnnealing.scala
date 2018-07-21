import scala.util.Random

class SimulatedAnnealing(firstState: STLState, server: HspiceServer, conf: Config, name: String) {
  Random.setSeed(1)

  def run(): STLState = {
    val states = for (i <- 0 until conf.saConf.stateNum)
      yield new SAState(firstState.createRandom(), server, conf, name + i)
    for (i <- 0 until conf.saConf.maxItr) {
      states.foreach(state => state.moveToNextState())
    }
    val best = states.maxBy(state => state.bestScore)
    println("Best score: " + best.bestScore)
    best.bestState
  }
}

object SimulatedAnnealing {
  def apply(firstState: STLState, server: HspiceServer, conf: Config, id: String):
  SimulatedAnnealing = new SimulatedAnnealing(firstState, server, conf, id)
}