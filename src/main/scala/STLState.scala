

case class STLState(spFile: SPFile, config: Config) {
  var score: Double = Double.MaxValue

  def calcScore(cmdRunner: CommandRunner): Double = {
    1.0
  }

  def createNeighbour(): STLState = {
    val newState = this.copy()
    newState.shiftSegment()
  }

  private def shiftSegment(): STLState = {
    val stlElements: List[STLElement] = spFile.getSTLElements(config)
    val newStlElements = stlElements.map(stlElement => stlElement.getNeighbour())
    spFile.setSTLElements(newStlElements)
    this
  }
}
