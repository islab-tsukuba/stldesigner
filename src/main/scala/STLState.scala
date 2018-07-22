import java.io.{File, FilenameFilter}

case class STLState(var spFile: SPFile, conf: Config, var id: Int) {
  val dirPath = "/dev/shm/"
  var score: Double = Double.MaxValue

  def calcScore(server: HspiceServer): Double = {
    if (score != Double.MaxValue) {
      return score
    }
    val hash = spFile.md5Hash
    val outputName = hash + "_" + id
    val spFilePath = dirPath + outputName + ".sp"
    val lisFilePath = dirPath + outputName + ".lis"
    spFile.writeToFile(spFilePath)
    server.runSpiceFile(spFilePath)
    val lisFile = LisFile(lisFilePath, conf)
    val evaluator = new EyeSizeEvaluator(new Config(), spFile.getTran())
    score = evaluator.evaluate(lisFile)
    deleteFileByPrefix(dirPath, outputName)
    score
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
    val newState = this.copy()
    newState.shiftSegment()
  }

  private def shiftSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.getNeighbour())
    val newSPFile = spFile.copy()
    newSPFile.setSTLElements(newStlElements)
    spFile = newSPFile
    this
  }

  def createRandom(): STLState = {
    val newState = this.copy(spFile = spFile.copy())
    newState.assignRandomSegment()
  }

  def assignRandomSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.assignRandom())
    val newSPFile = spFile.copy()
    newSPFile.setSTLElements(newStlElements)
    spFile = newSPFile
    this
  }
}
