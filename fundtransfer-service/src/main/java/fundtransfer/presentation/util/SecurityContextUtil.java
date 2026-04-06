package fundtransfer.presentation.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityContextUtil {

    // Hàm lấy Request hiện tại đang chạy
    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    // Hàm lấy thẳng số CIF
    public static String getCurrentCif() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            return request.getHeader("X-User-CIF");
        }
        return null;
    }
}