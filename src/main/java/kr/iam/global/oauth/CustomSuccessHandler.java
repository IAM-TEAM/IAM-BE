package kr.iam.global.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.iam.domain.member.dto.CustomOAuth2User;
import kr.iam.global.jwt.JWTUtil;
import kr.iam.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*60L);

        cookieUtil.createCookie("memberId", customUserDetails.getMemberId().toString(), response);
        cookieUtil.createCookie("channelId", customUserDetails.getChannelId().toString(), response);
        cookieUtil.createCookie("Authorization", token, response);
        //response.sendRedirect("https://hzpodcaster.com/NewMyPage");
        getRedirectStrategy().sendRedirect(request, response, "https://hzpodcaster.com/NewMyPage");
        //getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/login/oauth2/code/google");

    }

    private Cookie createCookie(String value) {

        Cookie cookie = new Cookie("Authorization", value);
        cookie.setMaxAge(60*60*60);
        cookie.setSecure(true); //https에서 사용
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}