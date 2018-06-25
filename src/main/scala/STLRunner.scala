object STLRunner {
  def main(args: Array[String]) {
    if (args.size == 0) {
      val server = HspiceServer()
      println("Init servers.")
      server.init(CommandRunner(), 8)
      println("Close servers.")
      server.close()
    } else {
      printf("Invalid args.")
    }
  }
}
