package bankadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(basePackages = "bankadmin.persistence.repository")
public class BankAdminServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAdminServiceApplication.class, args);
    }

}
