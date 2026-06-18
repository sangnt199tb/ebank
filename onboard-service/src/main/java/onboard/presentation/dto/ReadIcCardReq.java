package onboard.presentation.dto;

import lombok.Data;

@Data
public class ReadIcCardReq {
    private String phoneNumber;
    private String icFont;
    private String icBack;
}
