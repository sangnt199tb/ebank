package fundtransfer.presentation.service.impl;

import fundtransfer.presentation.exception.ErrorCode;
import fundtransfer.presentation.exception.TransferException;
import fundtransfer.presentation.model.TransferRequest;
import fundtransfer.presentation.service.FundTransferService;
import fundtransfer.presentation.util.SecurityContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FundTransferServiceImpl implements FundTransferService {
    private static Logger logger = LoggerFactory.getLogger(FundTransferServiceImpl.class);

    @Override
    public void transfer(TransferRequest requestBody) {
        String cifNumber = SecurityContextUtil.getCurrentCif();
        logger.info("Start FundTransferServiceImpl transfer with cifNumber: {}", SecurityContextUtil.getCurrentCif());
        cifNumber = null;
        if (cifNumber == null || cifNumber.isEmpty()) {
            throw new TransferException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("End FundTransferServiceImpl transfer success with cif: {}", cifNumber);
    }
}
