

class HspiceServer(cmdr: CommandRunner, conf: Config) {
  private val serverNum: Int = conf.hspiceServerNum
  private var serverPorts: Seq[Int] = Seq()
  private var portIndex = 0

  for (i <- 0 until serverNum) {
    serverRunner()
  }

  def getServerPorts(): Seq[Int] = serverPorts

  def getServerNum(): Int = serverNum

  def runSpiceFiles(paths: Seq[String]): Boolean = {
    for (path <- paths) {
      // TODO: run in multi thread.
      val exRes = runSpiceFile(path)
      if (exRes.result != 0) {
        return false
      }
    }
    true
  }

  def runSpiceFile(path: String): ExecResult = {
    val cmd = "hspice -CC %s -port %d -o %s"
      .format(path, serverPorts(portIndex), path.replace(".sp", ""))
    println("Command: " + cmd)
    portIndex += 1
    if (portIndex >= serverPorts.size) portIndex = 0
    cmdr.runCommand(cmd)
  }

  def close(): Unit = {
    for (port <- serverPorts) {
      val cmd = "hspice -CC -K -port " + port
      println("Command: " + cmd)
      val ret = cmdr.runCommand(cmd)
      if (ret.result != 0) {
        throw new Exception("Termination of hspice server is failed.")
      }
    }
    serverPorts = Seq()
  }

  private def serverRunner(): Unit = {
    val cmd = "hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt"
    println("Command: " + cmd)
    val ret = cmdr.runCommand(cmd)
    if (ret.result != 0) {
      throw new Exception("Initialization of hspice server is failed.")
    }
    serverPorts = serverPorts :+ getServerPort(ret.out)
  }

  private def getServerPort(out: Seq[String]): Int = {
    val r = """^Server is started on .*:(\d+)$""".r
    for (line <- out) {
      val m = r.findFirstMatchIn(line)
      if (m.isDefined) return m.get.group(1).toInt
    }
    0
  }

}
