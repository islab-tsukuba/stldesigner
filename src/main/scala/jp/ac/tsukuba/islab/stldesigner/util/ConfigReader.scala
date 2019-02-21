package jp.ac.tsukuba.islab.stldesigner.util

import java.io.FileReader

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

class ConfigReader {
  def getFromYAML(path: String): Config = {
    val reader = new FileReader(path)
    val mapper = new ObjectMapper(new YAMLFactory())
    mapper.readValue(reader, classOf[Config])
  }
}

object ConfigReader {
  def apply(): ConfigReader = new ConfigReader()
}
