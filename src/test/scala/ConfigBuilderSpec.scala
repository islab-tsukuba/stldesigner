import org.scalatest._

class ConfigBuilderSpec extends FlatSpec with DiagrammedAssertions {
  val builder = ConfigBuilder()
  "ConfigBuilder.getDefaultConfig()" should "return default config." in {
    val default = builder.getDefaultConfig()
    assert(default.hspiceServerNum === 8)
    assert(default.segmentLengthStep === 0.001)
    assert(default.segmentImpList === Seq(
      "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
      "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
      "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120"))
    assert(default.optimizeWeight === Map("optpt3" -> 1.0))
    assert(default.eyeTime === 2e-9)
    assert(default.eyeWidthMargin === 0.2)
    assert(default.saConf === SimulatedAnnealingConfig(4000, 0.01, 8))
    assert(default.name === "eye_size_multi_thread")
  }
}
