package jp.ac.tsukuba.islab.stldesigner.optimizer

import jp.ac.tsukuba.islab.stldesigner.util.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class SimulatedAnnealing(firstState: STLState, conf: Config) extends Optimizer(firstState, conf) {

  def run(): STLState = {
    val initTasks: Future[List[SAState]] = Future.sequence {
      (for (i <- 0 until conf.saConf.stateNum)
        yield {
          val initState = firstState.createNeighbour()
          Future {
            new SAState(initState, conf, conf.name, i + 1)
          }
        }).toList
    }
    var states = Await.result(initTasks, Duration.Inf)
    println("Gen: 0"
      + "\nScores: [" + states.map(state => state.score).mkString(" ") + "]"
      + "\nBest Scores: [" + states.map(state => state.bestScore).mkString(" ") + "]"
      + "\nProbavility: [" + states.map(state => state.probability).mkString(" ") + "]")
    for (i <- 0 until conf.saConf.maxItr) {
      val moveTask: Future[List[SAState]] = Future.sequence {
        states.map(state => state.moveToNextState())
      }
      states = Await.result(moveTask, Duration.Inf)
      println("Gen: " + i + 1
        + "\nScores: [" + states.map(state => state.score).mkString(" ") + "]"
        + "\nBest Scores: [" + states.map(state => state.bestScore).mkString(" ") + "]"
        + "\nProbavility: [" + states.map(state => state.probability).mkString(" ") + "]")
    }
    val best = states.maxBy(state => state.bestScore)
    println("Best score: " + best.bestScore)
    best.bestState
  }
}

object SimulatedAnnealing {
  def apply(firstState: STLState, conf: Config):
  SimulatedAnnealing = new SimulatedAnnealing(firstState, conf)
}