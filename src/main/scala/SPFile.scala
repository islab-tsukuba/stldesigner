import scala.io.Source

case class SPFile(path: String) {
  val firstSPFileContent: List[String] = Source.fromFile(path).getLines.toList
  val subSpaces: Seq[SubSpace] = getSubSpace()
  val stlWElement = getSTLWElements()

  private def getSubSpace(): Seq[SubSpace] = {
    // TODO: Create subspaces from sp file contents.
    Seq(
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

  private def getSTLWElements(): List[STLWElement] = {
    firstSPFileContent.filter(
      line => line.matches("""^W[0-9]+_STL_[0-9]+.*""")
    ).map(line => STLWElement(line))
  }
}
