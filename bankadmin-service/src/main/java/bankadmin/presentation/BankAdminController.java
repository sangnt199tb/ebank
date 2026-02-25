package bankadmin.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/bank-admin")
public class BankAdminController {
    @RequestMapping(method = RequestMethod.GET, value = "/test-bank-admin")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK bank admin";
    }
}
