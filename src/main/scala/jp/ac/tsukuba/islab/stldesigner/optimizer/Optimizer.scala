package jp.ac.tsukuba.islab.stldesigner.optimizer

import jp.ac.tsukuba.islab.stldesigner.circuit.{HspiceServer, SPFile}
import jp.ac.tsukuba.islab.stldesigner.util.Config

import scala.util.Random

abstract class Optimizer(firstState: STLState, conf: Config) {
  Random.setSeed(conf.randomSeed)

  def run(): STLState
}
