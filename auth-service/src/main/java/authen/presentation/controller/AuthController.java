package authen.presentation.controller;

import authen.presentation.model.LoginRequest;
import authen.presentation.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            logger.info("AuthController login with user: {}", request.getUsername());
            String token = authService.login(
                    request.getUsername(),
                    request.getPassword()
            );
            logger.info("AuthController login success with user: {}", request.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e){
            logger.error("AuthController login with error detail: {}", e);
            throw e;
        }
    }

}
