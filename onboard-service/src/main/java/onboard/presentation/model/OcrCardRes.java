package onboard.presentation.model;

import lombok.Data;

@Data
public class OcrCardRes {
    private String transId;
    private String icNumber;
    private String fullName;
    private String dob;
}
