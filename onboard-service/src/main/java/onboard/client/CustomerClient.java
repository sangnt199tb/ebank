package onboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/customer-service/customer/internal/onboard-call-customer")
    String callCustomerClient();
}
