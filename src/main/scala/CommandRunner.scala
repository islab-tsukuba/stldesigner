import scala.collection.mutable.ArrayBuffer
import scala.sys.process.{Process, ProcessLogger}

class CommandRunner {
  def runCommand(cmd: String): ExecResult = {
    val out = ArrayBuffer[String]()
    val err = ArrayBuffer[String]()

    val logger = ProcessLogger(
      (o: String) => out += o,
      (e: String) => err += e)

    val r = Process(cmd) ! logger

    return ExecResult(r, out.toList, err.toList)
  }
}

case class ExecResult(result: Int, out: List[String], err: List[String])

object CommandRunner {
  def apply(): CommandRunner = new CommandRunner()
}
