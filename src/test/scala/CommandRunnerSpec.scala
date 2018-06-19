import org.scalatest._

class CommandRunnerSpec extends FlatSpec with DiagrammedAssertions {
  val cmdr = CommandRunner()

  "run()" should "return result of exec." in {
    assert(cmdr.runCommand("echo hoge") === ExecResult(0, List("hoge"), List()))
    assert(cmdr.runCommand("echo fuga") === ExecResult(0, List("fuga"), List()))
  }
}
