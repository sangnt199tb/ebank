package onboard.persistence.service.impl;

import onboard.persistence.domain.OnboardingTransactionEntity;
import onboard.persistence.repository.OnboardingTransactionRepo;
import onboard.persistence.service.TransactionService;
import onboard.presentation.model.CheckPhoneEmailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final OnboardingTransactionRepo onboardingTransactionRepo;

    @Autowired
    public TransactionServiceImpl(OnboardingTransactionRepo onboardingTransactionRepo) {
        this.onboardingTransactionRepo = onboardingTransactionRepo;
    }

    @Override
    public OnboardingTransactionEntity postCreateTransaction(CheckPhoneEmailReq req) {
        OnboardingTransactionEntity entity = new OnboardingTransactionEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setEmail(req.getEmail());
        entity.setPhoneNumber(req.getPhoneNumber());
        entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        onboardingTransactionRepo.save(entity);
        return entity;
    }
}
