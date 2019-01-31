package jp.ac.tsukuba.islab.stldesigner.runner

import jp.ac.tsukuba.islab.stldesigner.optimizer.SimulatedAnnealing
import jp.ac.tsukuba.islab.stldesigner.circuit.{CommandRunner, HspiceServer}
import jp.ac.tsukuba.islab.stldesigner.util.ConfigReader

object STLRunner {
  def main(args: Array[String]) {
    if (args.size == 1) {
      println("Init servers.")
      val conf = ConfigReader().getFromYAML(args(0))
      val server = new HspiceServer(new CommandRunner(), conf)
      val sa = SimulatedAnnealing(server, conf)
      sa.run()
      println("Close servers.")
      server.close()
    } else {
      println("Invalid args. Format: sbt \"run [config path]\"")
    }
  }
}
