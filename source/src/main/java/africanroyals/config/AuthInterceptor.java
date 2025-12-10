package africanroyals.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // Allow access to public pages
        String path = request.getRequestURI();
        if (path.equals("/") || path.equals("/index") ||
                path.equals("/login") || path.equals("/signup") ||
                path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") ||
                path.startsWith("/auth/")) {
            return true;
        }

        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Check if accessing admin routes
        if (path.startsWith("/admin") || path.equals("/admin-catalog") ||
                path.equals("/add-item") || path.equals("/sales-report") ||
                path.equals("/promote-user")) {
            String role = (String) session.getAttribute("role");
            if (!"ADMIN".equals(role)) {
                // Non-admin users trying to access admin pages are redirected to catalog
                response.sendRedirect("/catalog");
                return false;
            }
        }

        return true;
    }
}

