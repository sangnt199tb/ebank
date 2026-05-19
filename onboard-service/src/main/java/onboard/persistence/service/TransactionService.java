package onboard.persistence.service;

import onboard.persistence.domain.OnboardingTransactionEntity;
import onboard.presentation.model.CheckPhoneEmailReq;

public interface TransactionService {
    OnboardingTransactionEntity postCreateTransaction(CheckPhoneEmailReq req);
}
