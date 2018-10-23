object STLRunner {
  def main(args: Array[String]) {
    if (args.size == 1) {
      println("Init servers.")
      val conf = ConfigBuilder().getFromYAML(args(0))
      val server = new HspiceServer(new CommandRunner(), conf)
      val firstState = STLState(SPFile(conf), conf, 0)
      val sa = SimulatedAnnealing(firstState, server, conf)
      sa.run()
      println("Close servers.")
      server.close()
    } else {
      printf("Invalid args. Format: mvn run [config path]")
    }
  }
}
