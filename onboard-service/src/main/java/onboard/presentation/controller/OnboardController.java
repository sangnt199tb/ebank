package onboard.presentation.controller;

import onboard.presentation.model.ConfigSdkModel;
import onboard.presentation.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/onboard")
public class OnboardController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(method = RequestMethod.GET, value = "/test-onboard")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK onboard roi do";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-all-config")
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigSdkModel> getAllConfigSdk(){
        return configService.getAllConfigSdk();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-one")
    @ResponseStatus(HttpStatus.OK)
    public ConfigSdkModel getConfigById(){
        return configService.getAllConfigSdkById();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/call-file-from-onboard")
    @ResponseStatus(HttpStatus.OK)
    public String getOnboardCallFile(){
        return configService.getOnboardCallFile();
    }
}
