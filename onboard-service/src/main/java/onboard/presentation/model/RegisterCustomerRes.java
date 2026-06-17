package onboard.presentation.model;

import lombok.Data;

@Data
public class RegisterCustomerRes {
    private String transId;
    private String transactionDate;
    private String userName;
    private String accountNumber;
    private String cifNumber;
    private String limitDay;
    private String limitMonth;
}
