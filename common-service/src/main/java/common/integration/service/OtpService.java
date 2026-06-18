package common.integration.service;

import common.integration.dto.OtpSendRequest;
import common.integration.dto.OtpSendResponse;
import common.integration.dto.ValidateOtpReq;
import common.integration.dto.ValidateOtpRes;

public interface OtpService {
    OtpSendResponse processAndSendOtp(OtpSendRequest request);

    ValidateOtpRes validateOtp(ValidateOtpReq request);
}
