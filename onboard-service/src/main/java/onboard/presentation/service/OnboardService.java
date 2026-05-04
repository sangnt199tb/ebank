package onboard.presentation.service;

import onboard.presentation.model.CaptchaResponse;
import onboard.presentation.model.CheckPhoneEmailReq;
import onboard.presentation.model.CheckPhoneEmailRes;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface OnboardService {
    ResponseEntity<CaptchaResponse> getCaptcha() throws IOException;

    ResponseEntity<CheckPhoneEmailRes> checkPhoneAndEmail(CheckPhoneEmailReq request);
}
