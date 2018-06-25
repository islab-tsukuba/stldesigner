import scala.util.Random

case class STLState(segments: Seq[Int]) {
  val MAX_SEGMENT_VAL = 19
  val MIN_SEGMENT_VAL = 1
  var score: Double = Double.MaxValue

  def calcScore(): Double = {
    if (score != Double.MaxValue) return score
    var sum = 0L
    for (seg <- segments) sum += seg
    // TODO: Normalize by max score.
    score = sum / (segments.length * 19.0)
    score
  }

  def createNeighbour(): STLState = {
    STLState(getShiftedSegment())
  }

  private def getShiftedSegment(): Seq[Int] = {
    segments.map(seg => {
      var s = seg + (Math.abs(Random.nextInt) % 3) - 1
      if (s > MAX_SEGMENT_VAL) s = MAX_SEGMENT_VAL
      else if (s < MIN_SEGMENT_VAL) s = MIN_SEGMENT_VAL
      s
    })
  }
}
