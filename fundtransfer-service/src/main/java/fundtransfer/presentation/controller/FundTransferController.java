package fundtransfer.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/fund-transfer")
public class FundTransferController {
    @RequestMapping(method = RequestMethod.GET, value = "/test-fund-transfer")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK fund transfer roi do";
    }
}
