package jp.ac.tsukuba.islab.stldesigner.util

import java.io.FileReader

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import scala.collection.JavaConverters._

class ConfigBuilder {
  def getDefaultConfig(): Config = {
    Config(
      "default",
      1,
      8,
      0.001,
      Seq(
        "Z30", "Z35", "Z40", "Z45", "Z50", "Z55",
        "Z60", "Z65", "Z70", "Z75", "Z80", "Z85",
        "Z90", "Z95", "Z100", "Z105", "Z110", "Z115", "Z120").asJava,
      Map("optpt3" -> 1.0).asJava,
      2e-9,
      0.2,
      SimulatedAnnealingConfig(4000, 0.01, 8, -1),
      "./data/template/template_W.sp")
  }

  def getFromYAML(path: String): Config = {
    val reader = new FileReader(path)
    val mapper = new ObjectMapper(new YAMLFactory())
    mapper.readValue(reader, classOf[Config])
  }
}

object ConfigBuilder {
  def apply(): ConfigBuilder = new ConfigBuilder
}
