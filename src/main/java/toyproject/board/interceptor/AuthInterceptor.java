package toyproject.board.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false); // 이미 존재하는 세션을 가져옴
        if (session == null) {
            response.sendError(401, "세션아이디가 필요합니다.");
            return false;
        }

        Object member = session.getAttribute("member");
        if (member == null) {
            response.sendError(401, "로그인이 필요합니다.");
            return false;
        }

        String userAgentAttribute = session.getAttribute("userAgent").toString();
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || !userAgent.equals(userAgentAttribute)) {
            response.sendError(401, "User-Agent 오류");
            return false;
        }

        String ipAttribute = session.getAttribute("ip").toString();
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        if (ip == null || !ip.equals(ipAttribute)) {
            response.sendError(401, "ip 오류");
            return false;
        }

        return true;

    }

}
