import java.io.{File, PrintWriter}

class StateLogger(outputPath: String) {
  val dir = new File(outputPath)
  if (!dir.exists()) dir.mkdirs()
  val logFilePath = new File(outputPath, "best.csv").getPath
  val logFile = new PrintWriter(logFilePath)
  logFile.write("gen, score, spFile\n")
  logFile.flush()

  def writeData(spFile: SPFile, evaluator: EyeSizeEvaluator, gen: Int, score: Double): Unit = {
    val spFilePath = new File(outputPath, "gen" + gen + ".sp")
    spFile.writeToFile(spFilePath)
    evaluator.writeEyeToFile(outputPath, "gen" + gen)
    logFile.write(gen + ", " + score + ", " + spFilePath + "\n")
    logFile.flush()
  }
}

object StateLogger {
  def apply(outputPath: String): StateLogger = new StateLogger(outputPath)
}
