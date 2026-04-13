package customer.integration.listener;

import customer.persistence.domain.CustomerEntity;
import customer.persistence.repository.CustomerRepository;
import customer.presentation.dto.UserDTO;
import customer.presentation.model.CustomerModel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/internal")
public class CustomerOnboardListener {
    private final CustomerRepository customerRepository;
    private static Logger logger = LoggerFactory.getLogger(CustomerOnboardListener.class);

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerOnboardListener(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/onboard-call-customer")
    public String callFile() {
        return "call customer ok roi do ban oi";
    }

    @GetMapping("/customers/{customerId}")
    public CustomerModel getCustomerById(@PathVariable("customerId") String customerId) {
        logger.info("Start CustomerOnboardListener getCustomerById with customerId: {}", customerId);
        CustomerEntity entity = customerRepository.findByCustomerId(customerId);

        if (entity == null) {
            throw new RuntimeException("Customer not found");
        }

        return modelMapper.map(entity, CustomerModel.class);
    }

    @GetMapping("/customers/users/{username}")
    public UserDTO getUserByUsername(@PathVariable("username") String username){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("sanglangthang");
        userDTO.setPassword("$2a$10$AYjj41H0H0R1Vh/7gBp19u2xPOiLp.I4v8ql2Bi4kMuYF1mgMwOxe");        userDTO.setRole("KH");
        return userDTO;
    }
}
