package fundtransfer.presentation.service;

import fundtransfer.presentation.model.TransferRequest;

public interface FundTransferService {
    void transfer(TransferRequest requestBody);
}
