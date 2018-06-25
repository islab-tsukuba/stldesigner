

class HspiceServer() {
  private var serverNum: Int = 0
  private var serverPorts: Seq[Int] = Seq()
  private var portIndex = 0
  private var cmdr: CommandRunner = _

  def init(cmdr: CommandRunner, config: Config): Unit = {
    this.cmdr = cmdr
    this.serverNum = config.hspiceServerNum
    for (i <- 0 until serverNum) {
      serverRunner()
    }
  }

  private def serverRunner(): Unit = {
    val ret = cmdr.runCommand("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
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
    val cmd = "hspice -C %s -port %d -o %s"
      .format(path, serverPorts(portIndex), path.replace(".sp", ""))
    portIndex += 1
    if (portIndex >= serverPorts.size) portIndex = 0
    cmdr.runCommand(cmd)
  }

  def close(): Unit = {
    for (port <- serverPorts) {
      val ret = cmdr.runCommand("hspice -CC -K -port " + port)
      if (ret.result != 0) {
        throw new Exception("Termination of hspice server is failed.")
      }
    }
    serverPorts = Seq()
  }

}

object HspiceServer {
  private val hspiceServer = new HspiceServer()

  def getInstance(): HspiceServer = {
    hspiceServer
  }

  def apply(): HspiceServer = hspiceServer
}
