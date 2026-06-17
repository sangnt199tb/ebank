package common.integration.dto;

import lombok.Data;

@Data
public class OtpSendRequest {
    private String phoneNumber;
    private String email;
}
