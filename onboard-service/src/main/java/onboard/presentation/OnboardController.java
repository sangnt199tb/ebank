package onboard.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/onboard")
public class OnboardController {
    @RequestMapping(method = RequestMethod.GET, value = "/test-onboard")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK onboard roi do";
    }
}
