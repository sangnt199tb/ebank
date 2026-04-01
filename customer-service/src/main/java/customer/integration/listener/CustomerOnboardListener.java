package customer.integration.listener;

import customer.persistence.domain.CustomerEntity;
import customer.persistence.repository.CustomerRepository;
import customer.presentation.model.CustomerModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/internal")
public class CustomerOnboardListener {
    private final CustomerRepository customerRepository;

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

        CustomerEntity entity = customerRepository.findByCustomerId(customerId);

        if (entity == null) {
            throw new RuntimeException("Customer not found");
        }

        return modelMapper.map(entity, CustomerModel.class);
    }
}
