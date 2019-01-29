package jp.ac.tsukuba.islab.stldesigner.spice

import org.scalatest._

class CommandRunnerTest extends FlatSpec with DiagrammedAssertions {
  val cmdr = new CommandRunner()

  "run()" should "return result of exec." in {
    assert(cmdr.runCommand("echo hoge") === ExecResult(0, Seq("hoge"), Seq()))
    assert(cmdr.runCommand("echo fuga") === ExecResult(0, Seq("fuga"), Seq()))
  }
}
