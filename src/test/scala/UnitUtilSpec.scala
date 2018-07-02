import org.scalatest._

class UnitUtilSpec extends FlatSpec with DiagrammedAssertions with Matchers {
  "WElement.strToDouble()" should "return decoded value." in {
    assert(UnitUtil.strToDouble("1.000000c") === Option(0.01))
    assert(UnitUtil.strToDouble("1.000000m") === Option(0.001))
    assert(UnitUtil.strToDouble("1.000000u") === Option(0.000001))
    assert(UnitUtil.strToDouble("1.000000n") === Option(0.000000001))
    assert(UnitUtil.strToDouble("1.000000p") === Option(0.000000000001))
    assert(UnitUtil.strToDouble("1.000000f") === Option(0.000000000000001))
    assert(UnitUtil.strToDouble("1.000000a") === Option(0.0))
    assert(UnitUtil.strToDouble("1.000000z") === Option(0.0))
    assert(UnitUtil.strToDouble("1.000000y") === Option(0.0))
    assert(UnitUtil.strToDouble("1c") === Option(0.01))
    assert(UnitUtil.strToDouble("1m") === Option(0.001))
    assert(UnitUtil.strToDouble("1u") === Option(0.000001))
    assert(UnitUtil.strToDouble("1n") === Option(0.000000001))
    assert(UnitUtil.strToDouble("1p") === Option(0.000000000001))
    assert(UnitUtil.strToDouble("1f") === Option(0.000000000000001))
    assert(UnitUtil.strToDouble("1a") === Option(0.0))
    assert(UnitUtil.strToDouble("1z") === Option(0.0))
    assert(UnitUtil.strToDouble("1y") === Option(0.0))
    assert(UnitUtil.strToDouble("hoge") === None)
  }
  "WElement.doubleToStr()" should "return encoded value." in {
    assert(UnitUtil.doubleToStr(0.001) === "1.000000m")
    assert(UnitUtil.doubleToStr(0.000001) === "1.000000u")
    assert(UnitUtil.doubleToStr(0.000000001) === "1.000000n")
    assert(UnitUtil.doubleToStr(0.000000000001) === "1.000000p")
    assert(UnitUtil.doubleToStr(0.000000000000001) === "1.000000f")
    assert(UnitUtil.doubleToStr(0.0) === "0.000000")
  }
}
