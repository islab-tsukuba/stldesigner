package jp.ac.tsukuba.islab.stldesigner.util

import org.scalatest._

import scala.collection.JavaConverters._

class ConfigBuilderTest extends FlatSpec with DiagrammedAssertions {
  val builder = ConfigReader()
  "getFromYAML()" should "return sa config." in {
    val conf = builder.getFromYAML(getClass().getResource("/config/test_sa.yml").getPath)
    assert(conf.name === "eye_size_multi_thread")
    assert(conf.randomSeed === 1)
    assert(conf.hspiceServerNum === 8)
    assert(conf.segmentLengthStep === 0.001)
    assert(conf.segmentImpList.asScala === Seq(
      "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
      "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
      "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120"))
    assert(conf.optimizeWeight.asScala === Map("optpt3" -> 1.0))
    assert(conf.eyeTime === 2e-9)
    assert(conf.eyeWidthMargin === 0.2)
    assert(conf.optimizationLogic === "sa")
    assert(conf.saConf === SimulatedAnnealingConfig(4000, 0.01, 8, -1))
    assert(conf.spFilePath === "./src/test/resources/template/template_W.sp")
  }

  "getFromYAML()" should "return ga config." in {
    val conf = builder.getFromYAML(getClass().getResource("/config/test_ga.yml").getPath)
    assert(conf.name === "eye_size_multi_thread")
    assert(conf.randomSeed === 1)
    assert(conf.hspiceServerNum === 8)
    assert(conf.segmentLengthStep === 0.001)
    assert(conf.segmentImpList.asScala === Seq(
      "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
      "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
      "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120"))
    assert(conf.optimizeWeight.asScala === Map("optpt3" -> 1.0))
    assert(conf.eyeTime === 2e-9)
    assert(conf.eyeWidthMargin === 0.2)
    assert(conf.optimizationLogic === "ga")
    assert(conf.gaConf === GeneticAlgorithmConfig(4000, 100, "blx_alpha", 0.366, "normal"))
    assert(conf.spFilePath === "./src/test/resources/template/template_W.sp")
  }
}