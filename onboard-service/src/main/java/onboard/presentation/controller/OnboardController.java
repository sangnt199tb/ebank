package onboard.presentation.controller;

import onboard.presentation.model.*;
import onboard.presentation.service.OnboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/onboard")
public class OnboardController {

    private final OnboardService onboardService;

    @Autowired
    public OnboardController(OnboardService onboardService) {
        this.onboardService = onboardService;
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() throws IOException {
        return onboardService.getCaptcha();
    }

    @PostMapping("/check-phone-email")
    public ResponseEntity<CheckPhoneEmailRes> checkPhoneAndEmail(@RequestBody CheckPhoneEmailReq request) {
        return onboardService.checkPhoneAndEmail(request);
    }

    @PostMapping("/send-otp-to-customer")
    public ResponseEntity<SendOtpToCustomerRes> sendOtpToCustomer(@RequestBody SendOtpToCustomerReq request) {
        return onboardService.sendOtpToCustomer(request);
    }

    @PostMapping("/confirm-otp")
    public ResponseEntity<ConfirmOtpRes> confirmOtp(@RequestBody ConfirmOtpReq request) {
        return onboardService.confirmOtp(request);
    }

    @PostMapping("/ocr-card")
    public ResponseEntity<OcrCardRes> ocrCard(@RequestBody OcrCardReq request) {
        return onboardService.ocrCard(request);
    }

    @PostMapping("/compare-face")
    public ResponseEntity<CompareFaceRes> compareFace(@RequestBody CompareFaceReq request) {
        return onboardService.compareFace(request);
    }

    @PostMapping("/confirm-info")
    public ResponseEntity<ConfirmInfoRes> confirmInfo(@RequestBody ConfirmInfoReq request) {
        return onboardService.confirmInfo(request);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<RegisterCustomerRes> registerCustomer(@RequestBody RegisterCustomerReq request) {
        return onboardService.registerCustomer(request);
    }
}
