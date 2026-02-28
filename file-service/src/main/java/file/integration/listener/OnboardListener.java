package file.integration.listener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file/internal")
public class OnboardListener {
    @GetMapping("/onboard-call-file")
    public String callFile() {
        return "call file ok roi do";
    }
}
