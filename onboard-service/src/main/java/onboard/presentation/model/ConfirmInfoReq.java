package onboard.presentation.model;

import lombok.Data;

@Data
public class ConfirmInfoReq {
    private String transId;
    private String icNumber;
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String oldIc;
    private String dateOfIssue;
    private String expirationDate;
}
