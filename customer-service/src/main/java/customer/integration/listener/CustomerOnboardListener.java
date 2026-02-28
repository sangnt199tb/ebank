package customer.integration.listener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/internal")
public class CustomerOnboardListener {
    @GetMapping("/onboard-call-customer")
    public String callFile() {
        return "call customer ok roi do ban oi";
    }
}
