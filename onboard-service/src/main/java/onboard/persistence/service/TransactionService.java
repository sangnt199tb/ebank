package onboard.persistence.service;

import onboard.persistence.domain.OnboardingTransactionEntity;
import onboard.presentation.dto.OnboardTransactionDto;
import onboard.presentation.model.CheckPhoneEmailReq;

public interface TransactionService {
    OnboardTransactionDto postCreateTransaction(CheckPhoneEmailReq req);
    OnboardTransactionDto getTransactionById(String transId);
    String updateTransactionId(OnboardTransactionDto onboardTransactionDto);
}
