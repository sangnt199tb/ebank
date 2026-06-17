package onboard.presentation.model;

import lombok.Data;

@Data
public class SendOtpToCustomerRes {
    private String transId;
    private String otpTransactionId;
}
