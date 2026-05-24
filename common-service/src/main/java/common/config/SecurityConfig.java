package common.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${api.internal.gateway-secret}")
    private String gatewaySecret;

    // Lấy danh sách API public từ file cấu hình của từng Service (nếu không có thì mặc định rỗng)
    @Value("${api.internal.public-endpoints:}")
    private String[] publicEndpoints;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // 1. THÊM BỘ LỌC CHẶN MẬT HIỆU LÊN TRƯỚC TIÊN
                .addFilterBefore(new GatewaySecretFilter(gatewaySecret), UsernamePasswordAuthenticationFilter.class)

                // 2. CẤU HÌNH PHÂN QUYỀN API
                .authorizeHttpRequests(auth -> {
                    // Mở cửa cho trang lỗi mặc định của Tomcat/Spring để tránh bị loop 401
                    auth.requestMatchers(new AntPathRequestMatcher("/error")).permitAll();

                    // Quét danh sách public API và dùng AntPathRequestMatcher
                    if (publicEndpoints != null) {
                        for (String endpoint : publicEndpoints) {
                            if (endpoint != null && !endpoint.trim().isEmpty()) {
                                // SỬ DỤNG ANTPATH THAY VÌ STRING MẶC ĐỊNH
                                auth.requestMatchers(new AntPathRequestMatcher(endpoint.trim())).permitAll();
                            }
                        }
                    }
                    auth.anyRequest().authenticated();
                });

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    // Filter tự chế để soi Mật Hiệu
    private static class GatewaySecretFilter extends OncePerRequestFilter {
        private final String secret;

        public GatewaySecretFilter(String secret) {
            this.secret = secret;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader("X-Internal-Gateway-Secret");
            if (!secret.equals(header)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Direct access is not allowed!");
                return;
            }
            filterChain.doFilter(request, response);
        }
    }
}