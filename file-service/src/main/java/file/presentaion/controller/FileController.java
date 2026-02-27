package file.presentaion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/file")
public class FileController {
    @RequestMapping(method = RequestMethod.GET, value = "/test-file")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK file roi do";
    }

    @GetMapping("/onboard-call-file")
    public String callFile() {
        return "File OK";
    }
}
