package onboard.presentation.client;

import onboard.presentation.dto.OtpSendRequest;
import onboard.presentation.dto.OtpSendResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "common-service")
public interface CommonClient {
    @PostMapping("/common-service/internal/api/v1/otp/send-otp")
    OtpSendResponse sendOtpToCustomer(OtpSendRequest otpSendRequest);
}
