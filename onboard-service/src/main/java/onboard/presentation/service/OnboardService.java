package onboard.presentation.service;

import onboard.presentation.model.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface OnboardService {
    ResponseEntity<CaptchaResponse> getCaptcha() throws IOException;

    ResponseEntity<CheckPhoneEmailRes> checkPhoneAndEmail(CheckPhoneEmailReq request);

    ResponseEntity<CompareFaceRes> compareFace(CompareFaceReq request);

    ResponseEntity<SendOtpToCustomerRes> sendOtpToCustomer(SendOtpToCustomerReq request);

    ResponseEntity<ConfirmOtpRes> confirmOtp(ConfirmOtpReq request);

    ResponseEntity<ConfirmInfoRes> confirmInfo(ConfirmInfoReq request);

    ResponseEntity<RegisterCustomerRes> registerCustomer(RegisterCustomerReq request);

    ResponseEntity<OcrCardRes> ocrCard(OcrCardReq request);
}
