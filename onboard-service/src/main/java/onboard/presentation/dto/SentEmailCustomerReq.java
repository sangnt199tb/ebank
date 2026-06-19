package onboard.presentation.dto;

import lombok.Data;

@Data
public class SentEmailCustomerReq {
    private String phoneNumber;
    private String email;
    private String fullName;
}
