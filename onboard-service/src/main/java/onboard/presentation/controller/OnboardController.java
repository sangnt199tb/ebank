package onboard.presentation.controller;

import onboard.presentation.model.*;
import onboard.presentation.service.OnboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping("/compare-face")
    public ResponseEntity<CompareFaceRes> compareFace(@RequestBody CompareFaceReq request) {
        return onboardService.compareFace(request);
    }
}
