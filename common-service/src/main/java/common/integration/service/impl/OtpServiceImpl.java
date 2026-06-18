package common.integration.service.impl;

import common.integration.dto.OtpSendRequest;
import common.integration.dto.OtpSendResponse;
import common.integration.dto.ValidateOtpReq;
import common.integration.dto.ValidateOtpRes;
import common.integration.service.OtpService;
import common.persitence.domain.OtpHistory;
import common.persitence.repository.OtpHistoryRepository;
import common.presentation.util.OtpStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class OtpServiceImpl implements OtpService {
    private static Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final OtpHistoryRepository otpHistoryRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public OtpServiceImpl(OtpHistoryRepository otpHistoryRepository, JavaMailSender mailSender) {
        this.otpHistoryRepository = otpHistoryRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public OtpSendResponse processAndSendOtp(OtpSendRequest request) {
        String transactionId = UUID.randomUUID().toString();
        String otpCode = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        OtpHistory history = OtpHistory.builder()
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .otpTransactionId(transactionId)
                .otpCode(otpCode)
                .expiresAt(expiresAt)
                .status(OtpStatus.PENDING)
                .build();
        otpHistoryRepository.save(history);

        try {
            sendEmail(request.getEmail(), otpCode);
            history.setStatus(OtpStatus.SENT);
            logger.info("OTP sent successfully to email: {}", request.getEmail());
        } catch (Exception e) {
            history.setStatus(OtpStatus.FAILED);
            logger.error("Failed to send OTP to email: {}", request.getEmail(), e);
        }

        OtpSendResponse otpSendResponse = new OtpSendResponse();
        otpSendResponse.setOtpTransactionId(transactionId);
        otpSendResponse.setStatus(history.getStatus().name());
        otpSendResponse.setMessage("OTP processing completed");
        return otpSendResponse;
    }

    @Override
    public ValidateOtpRes validateOtp(ValidateOtpReq request) {
        logger.info("OtpServiceImpl validateOtp with otp transId: {}", request.getOtpTransactionId());
        ValidateOtpRes res = new ValidateOtpRes();
        res.setStatusValidateOtp(false);
        try {
            OtpHistory otpHistory = otpHistoryRepository.findFirstByOtpTransactionId(request.getOtpTransactionId());
            if(Objects.isNull(otpHistory)){
                res.setStatusValidateOtp(false);
                return res;
            }

            if(StringUtils.equalsIgnoreCase(request.getOtpCode(), otpHistory.getOtpCode())){
                res.setStatusValidateOtp(true);
                return res;
            }
            return res;
        } catch (Exception e){
            logger.error("OtpServiceImpl validateOtp with error detail: {}", e);
            throw e;
        }
    }

    private void sendEmail(String to, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã Xác Nhận OTP Của Bạn");
        message.setText("Chào bạn,\n\nMã OTP của bạn là: " + otpCode + "\nMã này có hiệu lực trong vòng 5 phút. Vui lòng không chia sẻ cho bất kỳ ai.");
        mailSender.send(message);
    }
}
