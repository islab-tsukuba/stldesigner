package jp.ac.tsukuba.islab.stldesigner.util

object UnitUtil {
  def strToDouble(str: String): Option[Double] = {
    val noUnit = """^(-?[0-9]+\.?[0-9]*)$""".r
    val centi = """^(-?[0-9]+\.?[0-9]*)c$""".r
    val milli = """^(-?[0-9]+\.?[0-9]*)m$""".r
    val micro = """^(-?[0-9]+\.?[0-9]*)u$""".r
    val nano = """^(-?[0-9]+\.?[0-9]*)n$""".r
    val pico = """^(-?[0-9]+\.?[0-9]*)p$""".r
    val femt = """^(-?[0-9]+\.?[0-9]*)f$""".r
    val atto = """^(-?[0-9]+\.?[0-9]*)a$""".r
    val zepto = """^(-?[0-9]+\.?[0-9]*)z$""".r
    val yocto = """^(-?[0-9]+\.?[0-9]*)y$""".r
    str match {
      case noUnit(num) => Option(num.toDouble)
      case centi(num) => Option(num.toDouble * math.pow(10, -2))
      case milli(num) => Option(num.toDouble * math.pow(10, -3))
      case micro(num) => Option(num.toDouble * math.pow(10, -6))
      case nano(num) => Option(num.toDouble * math.pow(10, -9))
      case pico(num) => Option(num.toDouble * math.pow(10, -12))
      case femt(num) => Option(num.toDouble * math.pow(10, -15))
      case atto(num) => Option(0.0)
      case zepto(num) => Option(0.0)
      case yocto(num) => Option(0.0)
      case _ => None
    }
  }

  def doubleToStr(num: Double): String = {
    if (num >= math.pow(10, -3)) {
      "%.6f".format(num * math.pow(10, 3)) + "m"
    }
    else if (num >= math.pow(10, -6)) {
      "%.6f".format(num * math.pow(10, 6)) + "u"
    }
    else if (num >= math.pow(10, -9)) {
      "%.6f".format(num * math.pow(10, 9)) + "n"
    }
    else if (num >= math.pow(10, -12)) {
      "%.6f".format(num * math.pow(10, 12)) + "p"
    }
    else if (num >= math.pow(10, -15)) {
      "%.6f".format(num * math.pow(10, 15)) + "f"
    }
    else {
      "0.000000"
    }

  }
}
