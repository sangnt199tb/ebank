package onboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OnboardServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnboardServiceApplication.class, args);
    }
}
