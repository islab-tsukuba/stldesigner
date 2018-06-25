import scala.io.Source
import scala.util.Random

case class STLState(config: Config) {
  val MAX_SEGMENT_VAL = 19
  val MIN_SEGMENT_VAL = 1
  var score: Double = Double.MaxValue
  var firstSPFileContent = ""
  var subSpaces: Seq[SubSpace] = Seq()

  def setStateFromSPFile(path: String): Unit = {
    firstSPFileContent = Source.fromFile(path).getLines.mkString("\n")
    subSpaces = Seq(
      SubSpace(Seq(
        Segment(0.02, 50),
        Segment(0.02, 50),
        Segment(0.02, 50),
        Segment(0.02, 50),
        Segment(0.02, 50)), 0.1),
      SubSpace(Seq(
        Segment(0.07, 50),
        Segment(0.07, 50),
        Segment(0.07, 50),
        Segment(0.07, 50),
        Segment(0.07, 50)), 0.35),
      SubSpace(Seq(
        Segment(0.03, 50),
        Segment(0.03, 50),
        Segment(0.03, 50),
        Segment(0.03, 50),
        Segment(0.03, 50)), 0.15))
  }

  def calcScore(): Double = {
    if (score != Double.MaxValue) return score
    var sum = 0L
    for (sub <- subSpaces; seg <- sub.segments) sum += seg.impedance
    // TODO: Normalize by max score.
    score = sum / 50 / 15
    score
  }

  def createNeighbour(): STLState = {
    val state = STLState(config)
    state.setSubSpaces(getShiftedSegment())
    state
  }

  def setSubSpaces(subSpaces: Seq[SubSpace]): Unit = {
    this.subSpaces = subSpaces
  }

  private def getShiftedSegment(): Seq[SubSpace] = {
    subSpaces.map(sub => {
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
  }
}

case class SubSpace(segments: Seq[Segment], totalLength: Double)

case class Segment(length: Double, impedance: Int)
