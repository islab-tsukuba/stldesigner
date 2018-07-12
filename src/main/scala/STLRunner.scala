object STLRunner {
  def main(args: Array[String]) {
    if (args.size == 0) {
      println("Init servers.")
      val server = new HspiceServer(new CommandRunner(), new Config)
      println("Close servers.")
      server.close()
    } else {
      printf("Invalid args.")
    }
  }
}
