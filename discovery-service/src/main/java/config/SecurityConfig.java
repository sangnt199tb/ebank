package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Tắt CSRF để các service khác có thể gọi POST đăng ký vào Eureka
        http.csrf(AbstractHttpConfigurer::disable)
                // Yêu cầu mọi request đều phải có xác thực (nhập user/pass)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                // Sử dụng xác thực Basic Auth (Hộp thoại đăng nhập mặc định của trình duyệt)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}