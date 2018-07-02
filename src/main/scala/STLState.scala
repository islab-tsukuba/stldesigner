

case class STLState(spFile: SPFile, config: Config) {
  var score: Double = Double.MaxValue
  var stlElements: List[STLElement] = spFile.getSTLElements(config)

  def calcScore(): Double = {
    1.0
  }

  def createNeighbour(): STLState = {
    val newState = this.copy()
    newState.shiftSegment()
  }

  private def shiftSegment(): STLState = {
    stlElements = stlElements.map(stlElement => stlElement.getNeighbour())
    this
  }
}
