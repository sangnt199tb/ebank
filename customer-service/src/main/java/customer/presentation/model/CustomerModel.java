package customer.presentation.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CustomerModel {
    private String customerId;
    private String cifNumber;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String idNumber;
    private String icType;
    private String oldIcNumber;
    private String oldIcType;
    private LocalDate idIssueDate;
    private LocalDate idExpiryDate;
    private String idIssuePlace;
    private String permanentAddress;
    private String currentAddress;
    private String phoneNumber;
    private String email;
    private String nationality;
    private String branchCode;
    private String saleCode;
    private String referralCode;
    private String authenticationMethod;
    private String status;
    private Integer kycLevel;
    private String username;
    private String sourceApp;
    private String ageGroup;
    private String customerCategory;
    private String riskStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
