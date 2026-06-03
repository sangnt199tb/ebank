package onboard;

import common.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@EnableFeignClients
@SpringBootApplication
@Import(SecurityConfig.class)
public class OnboardServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnboardServiceApplication.class, args);
    }
}
