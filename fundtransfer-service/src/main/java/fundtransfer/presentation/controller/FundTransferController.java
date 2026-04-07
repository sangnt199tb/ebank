package fundtransfer.presentation.controller;

import fundtransfer.presentation.model.TransferRequest;
import fundtransfer.presentation.service.FundTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/fund-transfer")
public class FundTransferController {
    private final Logger logger = LoggerFactory.getLogger(FundTransferController.class);

    private final FundTransferService fundTransferService;

    @Autowired
    public FundTransferController(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test-fund-transfer")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK fund transfer roi do";
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestBody TransferRequest requestBody) {
        fundTransferService.transfer(requestBody);
        return "SUCCESS";
    }
}
