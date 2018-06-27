import org.scalatest._

import scala.io.Source

class SPFileSpec extends FlatSpec with DiagrammedAssertions with PrivateMethodTester {
  val spFile = SPFile("./src/test/resources/template/template_W.sp")

  "SPFile()" should "read file contents." in {
    val content = Source.fromFile("./src/test/resources/template/template_W.sp").getLines.toList
    assert(spFile.firstSPFileContent === content)
    assert(spFile.subSpaces ===
      Seq(
        SubSpace(Seq(
          Segment(0.02, 50),
          Segment(0.02, 50),
          Segment(0.02, 50),
          Segment(0.02, 50),
          Segment(0.02, 50)), 0.1),
        SubSpace(Seq(
          Segment(0.07, 50),
          Segment(0.07, 50),
          Segment(0.07, 50),
          Segment(0.07, 50),
          Segment(0.07, 50)), 0.35),
        SubSpace(Seq(
          Segment(0.03, 50),
          Segment(0.03, 50),
          Segment(0.03, 50),
          Segment(0.03, 50),
          Segment(0.03, 50)), 0.15))
    )
  }

  "getSTLWElements()" should "return STL element." in {
    val getSTLWElements: PrivateMethod[List[STLWElement]] = PrivateMethod[List[STLWElement]]('getSTLWElements)
    val elements = spFile invokePrivate getSTLWElements()
    assert(elements.map(element => element.line) === List(
      "W1_STL_5        102     0       optpt1  0       RLGCMODEL=Z50   N=1     L=100m",
      "W2_STL_5        optpt1  0       optpt2  0       RLGCMODEL=Z50   N=1     L=350m",
      "W3_STL_5        optpt2  0       optpt3  0       RLGCMODEL=Z50   N=1     L=150m"
    ))
  }
}
