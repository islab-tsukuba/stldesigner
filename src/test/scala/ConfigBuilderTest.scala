import org.scalatest._

import scala.collection.JavaConverters._

class ConfigBuilderTest extends FlatSpec with DiagrammedAssertions {
  val builder = ConfigBuilder()
  "ConfigBuilder.getDefaultConfig()" should "return default config." in {
    val default = builder.getDefaultConfig()
    assert(default.name === "eye_size_multi_thread")
    assert(default.hspiceServerNum === 8)
    assert(default.segmentLengthStep === 0.001)
    assert(default.segmentImpList.asScala === Seq(
      "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
      "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
      "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120"))
    assert(default.optimizeWeight.asScala === Map("optpt3" -> 1.0))
    assert(default.eyeTime === 2e-9)
    assert(default.eyeWidthMargin === 0.2)
    assert(default.saConf === SimulatedAnnealingConfig(4000, 0.01, 8))
    assert(default.spFilePath === "./data/template/template_W_akt_isolation_light.sp")
  }

  "ConfigBuilder.getFromYAML()" should "return yaml config." in {
    val conf = builder.getFromYAML("./src/test/resources/config/test.yml")
    assert(conf.name === "eye_size_multi_thread")
    assert(conf.hspiceServerNum === 8)
    assert(conf.segmentLengthStep === 0.001)
    assert(conf.segmentImpList.asScala === Seq(
      "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
      "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
      "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120"))
    assert(conf.optimizeWeight.asScala === Map("optpt3" -> 1.0))
    assert(conf.eyeTime === 2e-9)
    assert(conf.eyeWidthMargin === 0.2)
    assert(conf.saConf === SimulatedAnnealingConfig(4000, 0.01, 8))
    assert(conf.spFilePath === "./src/test/resources/template/template_W.sp")
  }
}
