package toyproject.board.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import toyproject.board.domain.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Member member1 = (Member) member;
        }

        String userAgent = request.getHeader("User-Agent");

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return true;
    }

}
