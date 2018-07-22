import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

class SimulatedAnnealing(firstState: STLState, server: HspiceServer, conf: Config) {
  Random.setSeed(1)

  def run(): STLState = {
    var states = (for (i <- 0 until conf.saConf.stateNum)
      yield new SAState(firstState.createNeighbour(), server, conf, conf.name, i + 1)).toList
    for (i <- 0 until conf.saConf.maxItr) {
      val moveTask: Future[List[SAState]] = Future.sequence {
        states.map(state => state.moveToNextState())
      }
      states = Await.result(moveTask, Duration.Inf)
      println("Gen: " + i + ", scores: [" + states.map(state => state.score).mkString(" ") + "]")
    }
    val best = states.maxBy(state => state.bestScore)
    println("Best score: " + best.bestScore)
    best.bestState
  }
}

object SimulatedAnnealing {
  def apply(firstState: STLState, server: HspiceServer, conf: Config):
  SimulatedAnnealing = new SimulatedAnnealing(firstState, server, conf)
}