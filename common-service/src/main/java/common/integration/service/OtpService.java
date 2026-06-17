package common.integration.service;

import common.integration.dto.OtpSendRequest;
import common.integration.dto.OtpSendResponse;

public interface OtpService {
    OtpSendResponse processAndSendOtp(OtpSendRequest request);
}
