package authen.integration.config; // Đã sửa theo vị trí thư mục mới

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // BẮT BUỘC PHẢI CÓ: Báo cho Spring biết đây là file cấu hình
public class SecurityConfig {

    @Bean // BẮT BUỘC PHẢI CÓ: Báo cho Spring biết hãy tạo Bean này đi
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}