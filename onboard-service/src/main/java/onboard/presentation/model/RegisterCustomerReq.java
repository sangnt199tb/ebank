package onboard.presentation.model;

import lombok.Data;

@Data
public class RegisterCustomerReq {
    private String transId;
    private String userName;
    private String password;
}
