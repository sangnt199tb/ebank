package authen.presentation.service.impl;

import authen.integration.client.UserClient;
import authen.presentation.dto.UserDTO;
import authen.presentation.exception.AuthException;
import authen.presentation.exception.ErrorCode;
import authen.presentation.service.AuthService;
import authen.presentation.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserClient userClient;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserClient userClient,
                           JwtUtil jwtUtil,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userClient = userClient;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String username, String password) {

        UserDTO user = userClient.getUserByUsername(username);
        logger.info("AuthServiceImpl login with user: {}", user.getUsername());

        if (user == null || Objects.isNull(user)) {
            throw new AuthException(ErrorCode.CONNECT_ERROR);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(ErrorCode.USER_PASS_ERROR);
        }

        return jwtUtil.generateToken(username, "KH", "123456788");
    }
}
