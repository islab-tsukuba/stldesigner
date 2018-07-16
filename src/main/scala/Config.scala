class Config {
  var hspiceServerNum = 8
  var segmentLengthStep = 0.001 // (1[mm])
  // TODO: Read from spice file.
  var segmentImpList = Seq(
    "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
    "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
    "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120")
  var optimizeWeight = Map("optpt3" -> 1.0)
  var eyeTime = 2e-9
  var eyeWidthMargin = 0.2

  def fromFile(path: String): Unit = {
  }
}
