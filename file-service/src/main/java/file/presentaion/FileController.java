package file.presentaion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/file")
public class FileController {
    @RequestMapping(method = RequestMethod.GET, value = "/test-file")
    @ResponseStatus(HttpStatus.OK)
    public String getInfoCompany(){
        return "OK file roi do";
    }
}
