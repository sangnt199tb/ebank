package customer.integration.listener;

import customer.persistence.domain.CustomerEntity;
import customer.persistence.repository.CustomerRepository;
import customer.presentation.dto.CustomerDto;
import customer.presentation.dto.RequestUserDto;
import customer.presentation.dto.UserDTO;
import customer.presentation.model.CustomerModel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/customer/internal")
public class CustomerOnboardListener {
    private final CustomerRepository customerRepository;
    private static Logger logger = LoggerFactory.getLogger(CustomerOnboardListener.class);
    private final BCryptPasswordEncoder passwordEncoder;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerOnboardListener(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
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
        try {
            logger.info("CustomerOnboardListener getUserByUsername username: {}", username);
            UserDTO userDTO = new UserDTO();
            CustomerEntity customerEntity = customerRepository.findByCustomerId(username);
            userDTO.setUsername(customerEntity.getCustomerId());
            userDTO.setPassword(customerEntity.getPassword());
            userDTO.setRole("KH");
            return userDTO;
        } catch (Exception e){
            logger.error("CustomerOnboardListener getUserByUsername with error detail: {}", e);
            throw e;
        }
    }

    @PostMapping("/customers/users/get-by-icNumber")
    public CustomerDto getUserByIcNumber(@RequestBody RequestUserDto requestUserDto) {
        logger.info("CustomerOnboardListener getUserByIcNumber requestUserDto: {}", requestUserDto);
        CustomerDto customerDto = new CustomerDto();
        CustomerEntity customerEntity = customerRepository.findFirstByIdNumber(requestUserDto.getIcNumber());
        if(Objects.nonNull(customerEntity)){
            customerDto.setFullName(customerEntity.getFullName());
            customerDto.setCifNumber(customerEntity.getCifNumber());
        }
        return customerDto;
    }

    @PostMapping("/customers/users/create-customer")
    public String createCustomer(@RequestBody CustomerDto customerDto){
        logger.error("CustomerOnboardListener getUserByIcNumber customerDto: {}", customerDto);
        try {
            SecureRandom secureRandom = new SecureRandom();
            int number = secureRandom.nextInt(100000000);
            String cifNumber = String.format("%08d", number);
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(cifNumber);
            customerEntity.setCifNumber(cifNumber);
            customerEntity.setFullName(customerDto.getFullName());
            customerEntity.setDateOfBirth(customerDto.getDateOfBirth());
            customerEntity.setBranchCode("888");
            customerEntity.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            customerRepository.save(customerEntity);
            return cifNumber;
        } catch (Exception e){
            logger.error("CustomerOnboardListener createCustomer with error detail: {}", e);
            throw e;
        }
    }
}
