import org.scalamock.scalatest.MockFactory
import org.scalatest._

class HspiceServerSpec extends FlatSpec with DiagrammedAssertions with PrivateMethodTester with MockFactory {
  val cmdr = stub[CommandRunner]
  val serverOut = (port: Int) => Seq("Using: /usr/synopsys/J-2014.09-SP1-2/hspice/amd64/hspice -CC",
    " lic:",
    " lic: FLEXlm: v10.9.8",
    " lic: USER:   odaira               HOSTNAME: 389b0610af9c",
    " lic: HOSTID: 0242ac110002         PID:      7106",
    " lic: Using FLEXlm license file:",
    " lic: /usr/synopsys/J-2014.09-SP1-2/admin/license/key",
    " lic: Checkout 1 hspice",
    " lic: License/Maintenance for hspice will expire on 31-mar-2019/2017.12",
    " lic: 51(in_use)/180(total) FLOATING license(s) on SERVER /usr/synopsys/J-2014.09-SP1-2/admin/licens",
    " lic:",
    "Hspice license have checked out",
    "***************************************",
    "*Welcome to HSPICE Client/Server Mode!*",
    "***************************************",
    "Server is started on 389b0610af9c:" + port)

  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25001), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25002), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25003), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25004), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25005), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25006), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25007), Seq())).once()
  (cmdr.runCommand _).when("hspice -CC >& /tmp/out.txt && sleep 2 && cat /tmp/out.txt")
    .returns(ExecResult(0, serverOut(25008), Seq())).once()
  val hServer = new HspiceServer(cmdr, new Config)
  "HspiceServer()" should "exec hspice servers and set server ports" in {
    assert(hServer.getServerPorts() === Seq(25001, 25002, 25003, 25004, 25005, 25006, 25007, 25008))
  }

  "getServerPort()" should "return the server port of hspice server" in {
    val getServerPort: PrivateMethod[Int] = PrivateMethod[Int]('getServerPort)
    val portNum = hServer invokePrivate getServerPort(serverOut(25001))
    assert(portNum === 25001)
  }

  "runSpiceFiles()" should "run hspice simulations" in {
    for (i <- 0 to 10) {
      (cmdr.runCommand _)
        .when("hspice -C ./sample%d.sp -port 2500%d -o ./sample%d".format(i, i % 8 + 1, i))
        .returns(ExecResult(0, Seq(), Seq()))
    }
    val result = hServer.runSpiceFile("./sample0.sp").result
    assert(result === 0)
    val ret = hServer.runSpiceFiles(for (i <- 1 to 10) yield "./sample%d.sp".format(i))
    assert(ret === true)
  }

  "close()" should "stop hspice servers" in {
    (cmdr.runCommand _).when("hspice -CC -K -port 25001").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25002").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25003").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25004").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25005").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25006").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25007").returns(ExecResult(0, Seq(), Seq()))
    (cmdr.runCommand _).when("hspice -CC -K -port 25008").returns(ExecResult(0, Seq(), Seq()))
    hServer.close()
    assert(hServer.getServerPorts() === Seq())
  }
}
