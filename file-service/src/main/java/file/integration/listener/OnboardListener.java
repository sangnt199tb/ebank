package file.integration.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file/internal")
public class OnboardListener {

    private static Logger logger = LoggerFactory.getLogger(OnboardListener.class);

    @GetMapping("/onboard-call-file")
    public String callFile() {
        logger.info("Start OnboardListener callFile");
        return "call file ok roi do";
    }
}
