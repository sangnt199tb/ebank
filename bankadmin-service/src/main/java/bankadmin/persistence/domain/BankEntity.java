package bankadmin.persistence.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BANK")
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "BANK_ID", length = 36)
    private String bankId;

    @Column(name = "BANK_CODE", nullable = false, length = 20)
    private String bankCode;

    @Column(name = "BANK_NAME", nullable = false, length = 150)
    private String bankName;

    @Column(name = "SWIFT_CODE", length = 20)
    private String swiftCode;

    @Column(name = "NAPAS_CODE", length = 20)
    private String napasCode;

    @Column(name = "STATUS", length = 20)
    private String status = "ACTIVE";

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // ===== Constructor =====
    public BankEntity() {}

    // ===== Getter & Setter =====
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getNapasCode() {
        return napasCode;
    }

    public void setNapasCode(String napasCode) {
        this.napasCode = napasCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}