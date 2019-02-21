package jp.ac.tsukuba.islab.stldesigner.util

import com.fasterxml.jackson.annotation.JsonProperty

case class Config(@JsonProperty(value = "name", required=true)
                  name: String,
                  @JsonProperty(value = "random_seed", required=false, defaultValue = "1")
                  randomSeed: Int,
                  @JsonProperty(value = "hspice_server_num", required=false, defaultValue = "1")
                  hspiceServerNum: Int,
                  @JsonProperty(value = "segment_length_step", required = true)
                  segmentLengthStep: Double,
                  @JsonProperty(value = "segment_imp_list", required = true)
                  segmentImpList: java.util.List[String],
                  @JsonProperty(value = "optimize_weight", required = true)
                  optimizeWeight: java.util.Map[String, Double],
                  @JsonProperty(value = "eye_time", required = true)
                  eyeTime: Double,
                  @JsonProperty(value = "eye_width_margin", required = true)
                  eyeWidthMargin: Double,
                  @JsonProperty(value = "optimization_logic", required = true)
                  optimizationLogic: String,
                  @JsonProperty(value = "sa_conf", required = false)
                  saConf: SimulatedAnnealingConfig,
                  @JsonProperty(value = "ga_conf", required = false)
                  gaConf: GeneticAlgorithmConfig,
                  @JsonProperty(value = "sp_file_path", required = true)
                  spFilePath: String)

case class SimulatedAnnealingConfig(@JsonProperty(value = "max_itr", required = true)
                                    var maxItr: Int,
                                    @JsonProperty(value = "target_temp", required = true)
                                    targetTemp: Double,
                                    @JsonProperty(value = "state_num", required = false, defaultValue = "1")
                                    stateNum: Int,
                                    @JsonProperty(value = "shift_segment_num", required = true)
                                    shiftSegmentNum: Int)

case class GeneticAlgorithmConfig(@JsonProperty(value = "max_itr", required = true)
                                  var maxItr: Int,
                                  @JsonProperty(value = "generation_size", required = true)
                                  var generationSize: Int,
                                  @JsonProperty(value = "mutation_probability", required = false, defaultValue = "0.01")
                                  var mutationProbabiliry: Double,
                                  @JsonProperty(value = "cross_algorithm", required = true)
                                  cross_algorithm: String,
                                  @JsonProperty(value = "blx_alpha", required = false, defaultValue = "0.366")
                                  blxAlpha: Double,
                                  @JsonProperty(value = "model", required = true)
                                  model: String)
