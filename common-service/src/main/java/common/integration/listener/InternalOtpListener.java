package common.integration.listener;

import common.integration.dto.*;
import common.integration.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/api/v1/otp")
@RequiredArgsConstructor
public class InternalOtpListener {
    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<OtpSendResponse> sendOtpInternal(@RequestBody OtpSendRequest request) {
        OtpSendResponse response = otpService.processAndSendOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<ValidateOtpRes> validateOtp(@RequestBody ValidateOtpReq request) {
        ValidateOtpRes response = otpService.validateOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sent-email-customer")
    public ResponseEntity<SentEmailCustomerRes> sentEmailToCustomer(@RequestBody SentEmailCustomerReq request) {
        SentEmailCustomerRes response = otpService.sentEmailToCustomer(request);
        return ResponseEntity.ok(response);
    }
}
