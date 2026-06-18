package onboard.presentation.client;

import onboard.config.FeignClientConfig;
import onboard.presentation.dto.OtpSendRequest;
import onboard.presentation.dto.OtpSendResponse;
import onboard.presentation.dto.ValidateOtpReq;
import onboard.presentation.dto.ValidateOtpRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "common-service", configuration = FeignClientConfig.class)
public interface CommonClient {
    @PostMapping("/common-service/internal/api/v1/otp/send-otp")
    OtpSendResponse sendOtpToCustomer(@RequestBody OtpSendRequest otpSendRequest);

    @PostMapping("/common-service/internal/api/v1/otp/validate-otp")
    ValidateOtpRes callCommonValidateOtp(@RequestBody ValidateOtpReq otpSendRequest);
}
