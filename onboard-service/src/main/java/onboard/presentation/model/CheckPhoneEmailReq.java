package onboard.presentation.model;

import lombok.Data;

@Data
public class CheckPhoneEmailReq {
    private String captchaId;
    private String captcha;
    private String phoneNumber;
    private String email;
    private String misCode;
}
