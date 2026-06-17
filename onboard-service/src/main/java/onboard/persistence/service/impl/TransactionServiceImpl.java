package onboard.persistence.service.impl;

import onboard.persistence.domain.OnboardingTransactionEntity;
import onboard.persistence.repository.OnboardingTransactionRepo;
import onboard.persistence.service.TransactionService;
import onboard.presentation.dto.OnboardTransactionDto;
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
    public OnboardTransactionDto postCreateTransaction(CheckPhoneEmailReq req) {
        OnboardingTransactionEntity entity = new OnboardingTransactionEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setEmail(req.getEmail());
        entity.setPhoneNumber(req.getPhoneNumber());
        entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        entity = onboardingTransactionRepo.save(entity);
        OnboardTransactionDto dto = new OnboardTransactionDto();
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public OnboardTransactionDto getTransactionById(String transId) {
        OnboardingTransactionEntity entity = onboardingTransactionRepo.findFirstByIdOrderByCreatedDateDesc(transId);
        return mapOnboardTransaction(entity);
    }

    @Override
    public String updateTransactionId(OnboardTransactionDto onboardTransactionDto) {
        return null;
    }

    private OnboardTransactionDto mapOnboardTransaction(OnboardingTransactionEntity entity){
        OnboardTransactionDto onboardTransactionDto = new OnboardTransactionDto();
        onboardTransactionDto.setId(entity.getId());
        onboardTransactionDto.setPhoneNumber(entity.getPhoneNumber());
        onboardTransactionDto.setIcNumber(entity.getIcNumber());
        onboardTransactionDto.setFullName(entity.getFullName());
        onboardTransactionDto.setAddress(entity.getAddress());
        onboardTransactionDto.setEmail(entity.getEmail());
        onboardTransactionDto.setDob(entity.getDob());
        onboardTransactionDto.setDateOfIssue(entity.getDateOfIssue());
        onboardTransactionDto.setExpirationDate(entity.getExpirationDate());
        onboardTransactionDto.setCreatedDate(entity.getCreatedDate());
        onboardTransactionDto.setUpdateDate(entity.getUpdateDate());
        onboardTransactionDto.setStatus(entity.getStatus());
        onboardTransactionDto.setStep(entity.getStep());
        return onboardTransactionDto;
    }
}
