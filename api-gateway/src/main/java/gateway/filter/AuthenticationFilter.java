package gateway.filter;

import gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    @Value("${api.gateway.open-endpoints}")
    private List<String> openEndpoints;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        logger.info("AuthenticationFilter filter with path: {}", path);

        // check api public
        boolean isPublicEndpoint = openEndpoints.stream().anyMatch(path::contains);
        logger.info("AuthenticationFilter filter isPublicEndpoint: {} and path: {}", isPublicEndpoint, path);

        if (isPublicEndpoint) {
            return chain.filter(exchange);
        }

        // CHECK HEADER AUTHORIZATION
        if (!request.getHeaders().containsKey("Authorization")) {
            logger.error("AuthenticationFilter filter containsKey Authorization with path: {}", path);
            return this.onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
        if (!authHeader.startsWith("Bearer ")) {
            logger.error("AuthenticationFilter filter bearer error with path: {}", path);
            return this.onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
        }

        // token
        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String cif = claims.get("cif", String.class);
            String role = claims.get("role", String.class);

            // 5. set info header
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-CIF", cif != null ? cif : "")
                    .header("X-User-Role", role != null ? role : "")
                    .build();

            logger.info("AuthenticationFilter filter success with path: {}", path);
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            logger.error("AuthenticationFilter filter error path: {} with error detail: {}", path, e);
            return this.onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
    }

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