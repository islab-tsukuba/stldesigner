import org.scalatest._

class SPFileSpec extends FlatSpec with DiagrammedAssertions {
  val conf = new Config()
  val spFile = SPFile("./src/test/resources/template/template_W.sp")

  "getSTLElements()" should "return STL element." in {
    val elements = spFile.getSTLElements(conf)
    assert(elements.map(element => element.line) === List(
      "W1_STL_5        102     0       optpt1  0       RLGCMODEL=Z50   N=1     L=100m",
      "W2_STL_5        optpt1  0       optpt2  0       RLGCMODEL=Z50   N=1     L=350m",
      "W3_STL_5        optpt2  0       optpt3  0       RLGCMODEL=Z50   N=1     L=150m"
    )
    )
    assert(elements.map(element => element.index) === List(14, 17, 20))
  }

  "setSTLElements()" should "sets STL element." in {
    val elements = spFile.getSTLElements(conf)
    spFile.setSTLElements(elements)
    assert(spFile.stlElements === elements)
  }
}
