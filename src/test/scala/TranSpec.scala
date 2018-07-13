import org.scalatest._

class TranSpec extends FlatSpec with Matchers {
  val tran = Tran(".TRAN 10p 24n 20n")
  "Tran" should "have tran parameters." in {
    assert(tran.resolution === (10e-12 +- 1e-20))
    assert(tran.endTime === (24e-9 +- 1e-20))
    assert(tran.startTime === (20e-9 +- 1e-20))
  }
}
