package fundtransfer.persistence.domain;

import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "TRANSFER")
public class TransferTransactionEntity {
    private String transferId;
    private String transactionCode;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private String creditAccountName;
    private String debitBankCode;
    private String creditBankCode;
    private BigDecimal amount;
}
