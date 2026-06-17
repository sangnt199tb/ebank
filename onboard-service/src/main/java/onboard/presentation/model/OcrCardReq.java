package onboard.presentation.model;

import lombok.Data;

@Data
public class OcrCardReq {
    private String transId;
    private String icFont;
    private String icBack;
}
