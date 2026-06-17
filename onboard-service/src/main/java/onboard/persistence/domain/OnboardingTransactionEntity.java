package onboard.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "onboarding_transaction")
@Data
public class OnboardingTransactionEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "ic_number")
    private String icNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
    private String dob;

    @Column(name = "date_of_issue")
    private String dateOfIssue;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "place_of_issue")
    private String placeOfIssue;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @Column(name = "status")
    private String status;

    @Column(name = "step")
    private String step;
}
