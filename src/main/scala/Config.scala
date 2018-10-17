case class Config(hspiceServerNum: Int = 8,
                  segmentLengthStep: Double = 0.001,
                  segmentImpList: Seq[String] = Seq(
                    "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
                    "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
                    "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120"),
                  optimizeWeight: Map[String, Double] = Map("optpt3" -> 1.0),
                  eyeTime: Double = 2e-9,
                  eyeWidthMargin: Double = 0.2,
                  saConf: SimulatedAnnealingConfig = SimulatedAnnealingConfig(4000, 0.01, 8),
                  name: String = "eye_size_multi_thread")

case class SimulatedAnnealingConfig(var maxItr: Int,
                                    targetTemp: Double,
                                    stateNum: Int)
