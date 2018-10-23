import com.fasterxml.jackson.annotation.JsonProperty

case class Config(@JsonProperty("name") name: String,
                  @JsonProperty("hspice_server_num") hspiceServerNum: Int,
                  @JsonProperty("segment_length_step") segmentLengthStep: Double,
                  @JsonProperty("segment_imp_list") segmentImpList: java.util.List[String],
                  @JsonProperty("optimize_weight") optimizeWeight: java.util.Map[String, Double],
                  @JsonProperty("eye_time") eyeTime: Double,
                  @JsonProperty("eye_width_margin") eyeWidthMargin: Double,
                  @JsonProperty("sa_conf") saConf: SimulatedAnnealingConfig,
                  @JsonProperty("sp_file_path") spFilePath: String)

case class SimulatedAnnealingConfig(@JsonProperty("max_itr") var maxItr: Int,
                                    @JsonProperty("target_temp") targetTemp: Double,
                                    @JsonProperty("state_num") stateNum: Int)
