import java.io.{File, FilenameFilter}

case class STLState(spFile: SPFile, conf: Config) {
  var score: Double = Double.MaxValue

  def calcScore(server: HspiceServer): Double = {
    if (score != Double.MaxValue) {
      return score
    }
    val hash = spFile.md5Hash
    val dirPath = "/dev/shm/"
    val fileName = hash + ".sp"
    val filePath = dirPath + fileName
    spFile.writeToFile(filePath)
    server.runSpiceFile(filePath)
    val lisFile = LisFile(filePath.replace(".sp", ".lis"), conf)
    val evaluator = new EyeSizeEvaluator(new Config(), spFile.getTran())
    score = evaluator.evaluate(lisFile)
    deleteFileByPrefix(dirPath, hash)
    score
  }

  private def deleteFileByPrefix(path: String, prefix: String): Unit = {
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
    spFile.setSTLElements(newStlElements)
    this
  }

  def createRandom(): STLState = {
    val newState = this.copy()
    newState.createRandom()
  }

  private def assignRandomSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements()
    val newStlElements = stlElements.map(stlElement => stlElement.assignRandom())
    spFile.setSTLElements(newStlElements)
    this
  }
}
