package onboard.presentation.dto;

import lombok.Data;

@Data
public class OtpSendResponse {
    private String otpTransactionId;
    private String status;
    private String message;
}
