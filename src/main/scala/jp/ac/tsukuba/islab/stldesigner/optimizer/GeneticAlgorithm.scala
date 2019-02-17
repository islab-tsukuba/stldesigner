package jp.ac.tsukuba.islab.stldesigner.optimizer

import jp.ac.tsukuba.islab.stldesigner.util.{Config, StateLogger}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

class GeneticAlgorithm(firstState: STLState, conf: Config) extends Optimizer(firstState, conf) {
  val logger = StateLogger("./output/" + conf.name + "/")
  var bestScore = firstState.score

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
    var population = Await.result(initTasks, Duration.Inf)
    println("Gen: 0"
      + "\nScores: [" + population.map(state => state.score).mkString(" ") + "]")
    var sortedStates = population.sortBy(_.score)
    println("Best score: " + sortedStates.head.score)
    for (i <- 0 until conf.gaConf.maxItr) {
      val moveTask: Future[List[STLState]] = Future.sequence {
        (for (_ <- 0 until conf.gaConf.generationSize) yield {
          val parents = rouletteChoice(population, 2)
          var child: STLState = null
          if (conf.gaConf.mutationProbabiliry > Random.nextDouble()) {
            child = parents.head.createCross(parents(1))
          } else {
            child = parents.head.createRandom()
          }
          Future {
            child.calcScore()
            child
          }
        }).toList
      }
      population = Await.result(moveTask, Duration.Inf)
      println("Gen: " + i
        + "\nScores: [" + population.map(state => state.score).mkString(" ") + "]")
      sortedStates = population.sortBy(_.score)
      val bestState = sortedStates.head
      println("Population best: " + bestState.score)
      if (bestScore > bestState.score) {
        logger.writeData(bestState.spFile, bestState.evaluator, i, bestState.score)
        bestScore = bestState.score
      }
    }
    sortedStates.head
  }

  private def rouletteChoice(states: Seq[STLState], choiceNum: Int = 1): Seq[STLState] = {
    val sortedState = mutable.ArrayBuffer(states.sortBy(_.score): _*)
    for (_ <- 0 until choiceNum) yield {
      var totalSum = 0.0
      sortedState.foreach(totalSum += 1.0 / _.score)
      val roulettePosition = Random.nextDouble() * totalSum
      var sum = 0.0
      var index = -1
      sortedState.zipWithIndex.foreach{
        case (state, i) =>
          sum += 1.0 / state.score
          if (sum >= roulettePosition && index == -1) index = i
      }
      val retState = sortedState(index)
      sortedState.remove(index)
      retState
    }
  }
}

object GeneticAlgorithm {
    def apply(firstState: STLState, conf: Config):
    GeneticAlgorithm = new GeneticAlgorithm(firstState, conf)
}
