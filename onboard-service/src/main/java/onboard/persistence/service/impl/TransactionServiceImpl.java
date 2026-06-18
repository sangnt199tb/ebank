package onboard.persistence.service.impl;

import onboard.persistence.domain.OnboardingTransactionEntity;
import onboard.persistence.repository.OnboardingTransactionRepo;
import onboard.persistence.service.TransactionService;
import onboard.presentation.dto.OnboardTransactionDto;
import onboard.presentation.model.CheckPhoneEmailReq;
import onboard.presentation.service.OnboardService;
import onboard.presentation.util.OnboardStatus;
import onboard.presentation.util.OnboardStep;
import org.apache.commons.lang.StringUtils;
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
        entity.setStatus(OnboardStatus.SUCCESS.name());
        entity.setStep(OnboardStep.INIT.name());
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
        OnboardingTransactionEntity entity
                = onboardingTransactionRepo.findFirstByIdOrderByCreatedDateDesc(onboardTransactionDto.getId());
        OnboardingTransactionEntity onboardingTransactionEntity = mapToOnboardEntity(onboardTransactionDto, entity);
        onboardingTransactionRepo.save(onboardingTransactionEntity);
        return entity.getId();
    }

    private OnboardingTransactionEntity mapToOnboardEntity(OnboardTransactionDto onboardTransactionDto, OnboardingTransactionEntity entity){
        if(StringUtils.isNotBlank(onboardTransactionDto.getPhoneNumber())){
            entity.setPhoneNumber(onboardTransactionDto.getPhoneNumber());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getIcNumber())){
            entity.setIcNumber(onboardTransactionDto.getIcNumber());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getFullName())){
            entity.setFullName(onboardTransactionDto.getFullName());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getAddress())){
            entity.setAddress(onboardTransactionDto.getAddress());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getEmail())){
            entity.setEmail(onboardTransactionDto.getEmail());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getDob())){
            entity.setDob(onboardTransactionDto.getDob());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getDateOfIssue())){
            entity.setDateOfIssue(onboardTransactionDto.getDateOfIssue());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getExpirationDate())){
            entity.setExpirationDate(onboardTransactionDto.getExpirationDate());
        }
        if(StringUtils.isNotBlank(onboardTransactionDto.getPlaceOfIssue())){
            entity.setPlaceOfIssue(onboardTransactionDto.getPlaceOfIssue());
        }
        return entity;
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
