import scala.util.Random

case class STLState(spFile: SPFile, config: Config) {
  var score: Double = Double.MaxValue
  var subSpaces: Seq[SubSpace] = spFile.subSpaces

  def calcScore(): Double = {
    var sum = 0L
    for (sub <- subSpaces; seg <- sub.segments) sum += seg.impedance
    // TODO: Normalize by max score.
    score = sum / 50 / 15
    score
  }

  def getScore(): Double = score

  def createNeighbour(): STLState = {
    val newState = this.copy()
    newState.shiftedSegment()
  }

  private def shiftedSegment(): STLState = {
    subSpaces = subSpaces.map(sub => {
      val oldLen = sub.totalLength
      var newLen: Double = 0
      val newSegments = sub.segments.map(seg => {
        var imp = seg.impedance + ((Math.abs(Random.nextInt) % 3) - 1) *
          config.segmentImpedanceStep
        if (imp > config.segmentImmpedanceMax) imp = config.segmentImmpedanceMax
        else if (imp < config.segmentImpedanceMin) imp = config.segmentImpedanceMin
        val len = seg.length + ((Math.abs(Random.nextInt) % 3) - 1) *
          config.segmentLengthStep
        newLen += len
        Segment(len, imp)
      }).map(seg => {
        // Adjust segment length
        val adjustRatio = oldLen / newLen
        Segment(seg.length * adjustRatio, seg.impedance)
      })
      SubSpace(newSegments, sub.totalLength)
    })
    this
  }
}

case class SubSpace(segments: Seq[Segment], totalLength: Double)

case class Segment(length: Double, impedance: Int)
