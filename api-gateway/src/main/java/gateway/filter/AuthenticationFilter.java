package gateway.filter;

import gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // ========================================================
    // 1. DANH SÁCH TRẮNG (WHITE-LIST)
    // Những đường dẫn chứa các từ khóa này sẽ KHÔNG cần Token
    // ========================================================
    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/auth-service/auth/login",
            "/onboard-service/",
            "/eureka"
    );

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        System.out.println("[Gateway] Đang kiểm duyệt Request: " + path);

        // 2. KIỂM TRA ĐỊNH TUYẾN MỞ CỬA
        boolean isPublicEndpoint = OPEN_ENDPOINTS.stream().anyMatch(path::contains);

        if (isPublicEndpoint) {
            System.out.println("[Gateway] => API Public (Onboard/Login), cho qua!");
            return chain.filter(exchange);
        }

        // 3. KIỂM TRA HEADER AUTHORIZATION
        if (!request.getHeaders().containsKey("Authorization")) {
            System.out.println("[Gateway] => Lỗi: Thiếu Header Authorization");
            return this.onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
        if (!authHeader.startsWith("Bearer ")) {
            System.out.println("[Gateway] => Lỗi: Token không có chữ Bearer");
            return this.onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
        }

        // 4. LẤY VÀ GIẢI MÃ TOKEN
        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String cif = claims.get("cif", String.class);
            String role = claims.get("role", String.class);

            // 5. GẮN THÔNG TIN VÀO HEADER CHO REQUEST NỘI BỘ
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-CIF", cif != null ? cif : "")
                    .header("X-User-Role", role != null ? role : "")
                    .build();

            System.out.println("[Gateway] => Xác thực thành công CIF: " + cif + ". Đang chuyển tiếp xuống Service...");
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            System.out.println("[Gateway] => Lỗi Token (Sai chữ ký hoặc Hết hạn): " + e.getMessage());
            return this.onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
    }

    // Hàm phụ trợ ném lỗi 401
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    // Ưu tiên chạy Filter này đầu tiên khi có request đến
    @Override
    public int getOrder() {
        return -1;
    }
}