import org.scalatest._

import scala.io.Source

class SPFileSpec extends FlatSpec {
  val conf = new Config()
  val spFile = SPFile("./src/test/resources/template/template_W.sp", conf)

  "getSTLElements()" should "return STL element." in {
    val elements = spFile.getSTLElements()
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
  "getString()" should "return all spice file content." in {
    assert(spFile.getString == newSpFile.getLines().mkString("\n"))
  }
}
