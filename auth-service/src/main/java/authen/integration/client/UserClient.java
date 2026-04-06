package authen.integration.client;

import authen.presentation.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface UserClient {
    @GetMapping("/customer-service/customer/internal/customers/users/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);
}
