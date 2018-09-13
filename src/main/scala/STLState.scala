import java.io.{File, FilenameFilter}

case class STLState(var spFile: SPFile, conf: Config, var id: Int) {
  val dirPath = "/dev/shm/"
  var score: Double = Double.MaxValue
  var evaluator: EyeSizeEvaluator = null
  var firstScore: Double = Double.MaxValue

  def calcScore(server: HspiceServer): Double = {
    if (score != Double.MaxValue && firstScore != Double.MaxValue) {
      return score / firstScore
    }
    if (firstScore == Double.MaxValue) {
      throw new Exception("FirstScore is not calculated. You should run calcFirstScore Function.")
    }
    val hash = spFile.md5Hash
    val outputName = hash + "_" + id
    val spFilePath = new File(dirPath, outputName + ".sp")
    val lisFilePath = new File(dirPath, outputName + ".lis")
    spFile.writeToFile(spFilePath)
    server.runSpiceFile(spFilePath.getPath)
    val lisFile = LisFile(lisFilePath.getPath, conf)
    evaluator = new EyeSizeEvaluator(lisFile, new Config(), spFile.getTran())
    score = evaluator.evaluate()
    deleteFileByPrefix(dirPath, outputName)
    score / firstScore
  }

  def deleteFileByPrefix(path: String, prefix: String): Unit = {
    val dir = new File(path)
    val files = dir.listFiles(
      new FilenameFilter() {
        def accept(dir: File, name: String): Boolean = name.matches(prefix + ".*")
      })
    files.foreach(file => {
      file.delete()
    })
  }

  def createNeighbour(): STLState = {
    val newState = this.copy(spFile = spFile.deepCopy())
    newState.shiftSegment()
  }

  private def shiftSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.getNeighbour())
    spFile.setSTLElements(newStlElements)
    this
  }

  def createRandom(): STLState = {
    val newState = this.copy(spFile = spFile.deepCopy())
    newState.assignRandomSegment()
  }

  def assignRandomSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.assignRandom())
    spFile.setSTLElements(newStlElements)
    this
  }

  def writeData(dirPath: String, fileName: String): Unit = {
    if (evaluator == null) {
      throw new Exception("Evaluator is not created. You should run calcScore Function.")
    }
    val dir = new File(dirPath)
    if (!dir.exists()) dir.mkdir()
    val spFilePath = new File(dirPath, fileName + ".sp")
    spFile.writeToFile(spFilePath)
    evaluator.writeEyeToFile(dirPath, fileName)
  }

  def calcFirstScore(server: HspiceServer): Double = {
    val outputName = "first"
    val spFilePath = new File(dirPath, outputName + ".sp")
    val lisFilePath = new File(dirPath, outputName + ".lis")
    spFile.writeFirstToFile(spFilePath)
    server.runSpiceFile(spFilePath.getPath)
    val lisFile = LisFile(lisFilePath.getPath, conf)
    val evaluator = new EyeSizeEvaluator(lisFile, new Config(), spFile.getTran())
    firstScore = evaluator.evaluate()
    deleteFileByPrefix(dirPath, outputName)
    firstScore
  }
}
