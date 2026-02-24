package customer.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customer")
public class CustomerController {
    @RequestMapping(method = RequestMethod.GET, value = "/test-customer")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK rồi";
    }
}
