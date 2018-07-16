object STLRunner {
  def main(args: Array[String]) {
    if (args.size == 0) {
      println("Init servers.")
      val server = new HspiceServer(new CommandRunner(), new Config)
      val conf = new Config()
      val firstState = STLState(SPFile("./template/template_W.sp", conf), conf)
      val sa = SimulatedAnnealing(firstState, 1000, 0.1, 0.3, server)
      sa.run()
      println("Close servers.")
      server.close()
    } else {
      printf("Invalid args.")
    }
  }
}
