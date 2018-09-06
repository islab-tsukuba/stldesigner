import java.io.{File, PrintWriter}

import scala.collection.mutable
import scala.util.control.Breaks

trait SignalEvaluator {
  def evaluate(): Double
}

class EyeSizeEvaluator(lisFile: LisFile, conf: Config, tran: Tran) extends SignalEvaluator {
  val eyeSize = (conf.eyeTime / tran.resolution).toInt

  override def evaluate(): Double = {
    var score = 0.0
    for ((k, v) <- conf.optimizeWeight) {
      val vList = lisFile.getVoltage(k)
      val singlePointScore = calcSinglePoint(vList)
      score += singlePointScore * v
    }
    score
  }

  private def calcSinglePoint(vList: Seq[Double]): Double = {
    val eyeDiagram = getEyeLines(vList)

    // Calc eye height.
    val heightRangeStart = (eyeSize * conf.eyeWidthMargin).toInt
    val heightRangeEnd = (eyeSize * (1 - conf.eyeWidthMargin)).toInt
    var convexPoint = -1
    var minVolt = Double.MinValue
    val eyeUnder = eyeDiagram(1)
    val eyeUpper = eyeDiagram(2)
    // Search the convex upwards and get max of these values.
    for (i <- heightRangeStart until heightRangeEnd - 2) {
      if (eyeUnder(i) < eyeUnder(i + 1) && eyeUnder(i + 1) > eyeUnder(i + 2) &&
        minVolt < eyeUnder(i + 1)) {
        minVolt = eyeUnder(i + 1)
        convexPoint = i + 1
      }
    }
    // Set the minimum convex point if the convex upwards don't exists.
    if (convexPoint == -1) {
      convexPoint = eyeUnder.zipWithIndex.slice(heightRangeStart, heightRangeEnd).min._2
    }
    // Get eye height from sub of convex point.
    val eyeHeight = eyeUpper(convexPoint) - eyeUnder(convexPoint)

    // Calc eye width.
    var eyeWidthStart = 0
    var eyeWidthEnd = 0
    val b = new Breaks
    b.breakable {
      for (i <- 0 until eyeSize) {
        eyeWidthStart = i
        if (eyeUnder(i) < eyeUpper(i)) {
          b.break
        }
      }
    }
    b.breakable {
      for (i <- eyeWidthStart until eyeSize) {
        eyeWidthEnd = i
        if (eyeUnder(i) >= eyeUpper(i)) {
          b.break
        }
      }
    }
    val eyeWidth = (eyeWidthEnd - eyeWidthStart) * tran.resolution / conf.eyeTime

    if ((eyeHeight + eyeWidth) <= 0) {
      return Double.MaxValue
    }
    1.0 / (eyeHeight + eyeWidth)
  }

  private def getEyeLines(vList: Seq[Double]): Seq[Seq[Double]] = {
    val eyeDiagram = Seq.fill(4)(mutable.Seq.fill(eyeSize)(0.0))
    val maxVolts = mutable.Seq.fill(eyeSize)(0.0)

    // Create virtual eye diagram.
    for (i <- 0 until vList.size) {
      val eyePos = i % eyeSize
      if (vList(i) > 0.0) {
        eyeDiagram(0)(eyePos) += vList(i)
      } else {
        eyeDiagram(3)(eyePos) += vList(i)
      }
      if (vList(i) > maxVolts(eyePos)) {
        maxVolts(eyePos) = vList(i)
      }
    }
    var maxVolt = 0.0
    var eyeStart = 0
    for (i <- 0 until eyeSize) {
      eyeDiagram(1)(i) = eyeDiagram(0)(i) - maxVolts(i)
      eyeDiagram(2)(i) = eyeDiagram(3)(i) + maxVolts(i)
      if (maxVolt < eyeDiagram(1)(i)) {
        maxVolt = eyeDiagram(1)(i)
        eyeStart = i
      }
    }

    // Shift eye diagram.
    eyeDiagram.map(valts => valts.drop(eyeStart) ++ valts.take(eyeStart))
  }

  def writeEyeToFile(dirPath: String, fileName: String): Unit = {
    for ((k, v) <- conf.optimizeWeight) {
      val lisFilePath = new File(dirPath, fileName + "_" + k + ".dat")
      val writer = new PrintWriter(lisFilePath)
      val eyeDiagram = getEyeLines(lisFile.getVoltage(k))
      writer.write("v1\tv2\tv3\tv4\n")
      for (i <- 0 until eyeSize) {
        writer.write(eyeDiagram(0)(i) + "\t" +
          eyeDiagram(1)(i) + "\t" +
          eyeDiagram(2)(i) + "\t" +
          eyeDiagram(3)(i) + "\n")
      }
      writer.close()
    }
  }
}
