import org.scalatest._

import scala.io.Source

class SPFileSpec extends FlatSpec with DiagrammedAssertions {
  val conf = new Config()
  val spFile = SPFile("./src/test/resources/template/template_W.sp", conf)

  "getFirstSTLElements()" should "return STL element." in {
    val elements = spFile.getFirstSTLElements()
    assert(elements.map(element => element.line) === List(
      "W1_STL_5        102     0       optpt1  0       RLGCMODEL=Z50   N=1     L=100m",
      "W2_STL_5        optpt1  0       optpt2  0       RLGCMODEL=Z50   N=1     L=350m",
      "W3_STL_5        optpt2  0       optpt3  0       RLGCMODEL=Z50   N=1     L=150m"
    )
    )
    assert(elements.map(element => element.index) === List(14, 17, 20))
  }

  "setSTLElements()" should "sets STL element." in {
    val elements = spFile.getSTLElements()
    spFile.setSTLElements(elements)
    assert(spFile.stlElements === elements)
  }

  val newSpFile = Source.fromFile("./src/test/resources/template/template_W_separated.sp")
  val newFileStr = newSpFile.getLines().mkString("\n")
  "getString()" should "return all spice file content." in {
    assert(spFile.getString === newFileStr)
  }

  "getTran()" should "return trun command of spice file." in {
    assert(spFile.getTran === Tran(".TRAN 10p 24n 20n"))
  }
}