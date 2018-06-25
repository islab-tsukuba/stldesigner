class Config {
  var hspiceServerNum = 8
  var segmentImpedanceMin = 30
  var segmentImmpedanceMax = 120
  var segmentImpedanceStep = 5
  var segmentLengthStep = 0.001 // (1[mm])

  def fromFile(path: String): Unit = {
  }
}
