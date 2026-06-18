package common.integration.dto;

import lombok.Data;

@Data
public class ValidateOtpReq {
    private String otpCode;
    private String otpTransactionId;
}
