package onboard.presentation.client;

import onboard.presentation.model.CustomerModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/customer-service/customer/internal/onboard-call-customer")
    String callCustomerClient();

    @GetMapping("/customer-service/customer/internal/customers/{customerId}")
    CustomerModel getCustomerById(@PathVariable("customerId") String customerId);
}
