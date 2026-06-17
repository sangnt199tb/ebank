package onboard.presentation.model;

import lombok.Data;

@Data
public class ConfirmOtpReq {
    private String transId;
    private String otpCode;
    private String otpTransactionId;
}
