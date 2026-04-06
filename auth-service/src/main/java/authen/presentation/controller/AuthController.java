package authen.presentation.controller;

import authen.presentation.model.LoginRequest;
import authen.presentation.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Nhập mật khẩu bạn muốn băm vào đây (ví dụ: chuỗi bạn vừa gửi)
//        String rawPassword = "hd1i21389k1j31031931nhuh91j1nu";

        // Nếu bạn vẫn muốn dùng pass cũ thì đổi thành:
         String rawPassword = "Putin@140223";

        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Mật khẩu gốc: " + rawPassword);
        System.out.println("Chuỗi BCrypt copy vào mock: " + encodedPassword);

        try {
            System.out.println("Start AuthController login");
            String token = authService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e){
            System.out.println("AuthController login with error detail: {}" + e);
            throw e;
        }
    }

}
