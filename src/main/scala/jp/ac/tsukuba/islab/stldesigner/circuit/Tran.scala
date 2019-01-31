package jp.ac.tsukuba.islab.stldesigner.circuit

import jp.ac.tsukuba.islab.stldesigner.util.UnitUtil

case class Tran(line: String) {
  val splitLine: Array[String] = line.split("""\s+""")
  val resolution: Double = UnitUtil.strToDouble(splitLine(1)).getOrElse(0.0)
  val endTime: Double = UnitUtil.strToDouble(splitLine(2)).getOrElse(0.0)
  val startTime: Double = UnitUtil.strToDouble(splitLine(3)).getOrElse(0.0)
}
