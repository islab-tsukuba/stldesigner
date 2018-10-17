class ConfigBuilder {
  def getDefaultConfig(): Config = {
    Config()
  }
}

object ConfigBuilder {
  def apply(): ConfigBuilder = new ConfigBuilder
}
