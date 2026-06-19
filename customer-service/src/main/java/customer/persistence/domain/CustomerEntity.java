package customer.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {
    @Id
    @Column(name = "CUSTOMER_ID", length = 36)
    private String customerId;

    @Column(name = "CIF_NUMBER")
    private String cifNumber;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ID_NUMBER")
    private String idNumber;

    @Column(name = "IC_TYPE")
    private String icType;

    @Column(name = "OLD_IC_NUMBER")
    private String oldIcNumber;

    @Column(name = "OLD_IC_TYPE")
    private String oldIcType;

    @Column(name = "ID_ISSUE_DATE")
    private LocalDate idIssueDate;

    @Column(name = "ID_EXPIRY_DATE")
    private LocalDate idExpiryDate;

    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;

    @Column(name = "PERMANENT_ADDRESS")
    private String permanentAddress;

    @Column(name = "CURRENT_ADDRESS")
    private String currentAddress;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL_NHOM07")
    private String email;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "SALE_CODE")
    private String saleCode;

    @Column(name = "REFERRAL_CODE")
    private String referralCode;

    @Column(name = "AUTHENTICATION_METHOD", length = 100)
    private String authenticationMethod;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "KYC_LEVEL")
    private Integer kycLevel;

    @Column(name = "USERNAME", length = 100)
    private String username;

    @Column(name = "SOURCE_APP", length = 100)
    private String sourceApp;

    @Column(name = "AGE_GROUP", length = 20)
    private String ageGroup;

    @Column(name = "CUSTOMER_CATEGORY", length = 20)
    private String customerCategory;

    @Column(name = "RISK_STATUS", length = 20)
    private String riskStatus;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "PASSWORD")
    private String password;
}
