package onboard.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompareFaceInterReq {
    @JsonProperty("idImage")
    private String idImage;

    @JsonProperty("selfieImage")
    private String selfieImage;
}
