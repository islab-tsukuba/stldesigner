object STLRunner {
  def main(args: Array[String]) {
    if (args.size == 0) {
      println("Init servers.")
      val server = new HspiceServer(new CommandRunner(), new Config)
      val conf = new Config()
      val firstState = STLState(SPFile("./data/template/template_W_akt_isolation_light.sp", conf), conf)
      val sa = SimulatedAnnealing(firstState, 32000, 0.01, 0.3, server, "eye_size")
      sa.run()
      println("Close servers.")
      server.close()
    } else {
      printf("Invalid args.")
    }
  }
}
