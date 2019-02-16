package jp.ac.tsukuba.islab.stldesigner.optimizer

import jp.ac.tsukuba.islab.stldesigner.circuit.HspiceServer
import jp.ac.tsukuba.islab.stldesigner.util.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class GeneticAlgorithm(firstState: STLState, conf: Config) extends Optimizer(firstState, conf) {

  def run: STLState = {
    val initTasks: Future[List[STLState]] = Future.sequence {
      (for (i <- 0 until conf.gaConf.generationSize)
        yield {
          val initState = firstState.createRandom()
          Future {
            initState.calcScore()
            initState
          }
        }).toList
    }
    var states = Await.result(initTasks, Duration.Inf)
    println("Gen: 0"
      + "\nScores: [" + states.map(state => state.score).mkString(" ") + "]")
    var sortedStates = states.sortBy(_.score)
    println("Best score: " + sortedStates.head.score)

    sortedStates.head
  }

  def rouletteChoice(list: Seq[STLState]): Unit = {

  }
}

object GeneticAlgorithm {
    def apply(firstState: STLState, conf: Config):
    GeneticAlgorithm = new GeneticAlgorithm(firstState, conf)
}
