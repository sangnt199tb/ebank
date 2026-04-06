package fundtransfer.presentation.controller;

import fundtransfer.presentation.model.TransferRequest;
import fundtransfer.presentation.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/fund-transfer")
public class FundTransferController {
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
    public String thucHienChuyenTien(@RequestBody TransferRequest requestBody) {
        fundTransferService.transfer(requestBody);
        return "Thành công!";
    }
}
