package common.integration.service;

import common.integration.dto.*;

public interface OtpService {
    OtpSendResponse processAndSendOtp(OtpSendRequest request);

    ValidateOtpRes validateOtp(ValidateOtpReq request);

    SentEmailCustomerRes sentEmailToCustomer(SentEmailCustomerReq request);
}
