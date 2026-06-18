package onboard.presentation.client;

import customer.presentation.dto.CustomerDto;
import customer.presentation.dto.RequestUserDto;
import customer.presentation.model.CustomerModel;
import onboard.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customer-service", configuration = FeignClientConfig.class)
public interface CustomerClient {
    @GetMapping("/customer-service/customer/internal/onboard-call-customer")
    String callCustomerClient();

    @GetMapping("/customer-service/customer/internal/customers/{customerId}")
    CustomerModel getCustomerById(@PathVariable("customerId") String customerId);

    @PostMapping("/customer-service/customer/internal/customers/users/get-by-icNumber")
    CustomerDto getCustomerByIcNumber(@RequestBody RequestUserDto requestUserDto);

}
