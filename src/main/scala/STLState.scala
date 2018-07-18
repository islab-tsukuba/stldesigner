

case class STLState(spFile: SPFile, conf: Config) {
  var score: Double = Double.MaxValue

  def calcScore(server: HspiceServer): Double = {
    val hash = math.abs(spFile.hashCode())
    val filePath = "/dev/shm/" + hash + ".sp"
    spFile.writeToFile(filePath)
    server.runSpiceFile(filePath)
    val lisFile = LisFile(filePath.replace(".sp", ".lis"), conf)
    val evaluator = new EyeSizeEvaluator(new Config(), spFile.getTran())
    evaluator.evaluate(lisFile)
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
}
