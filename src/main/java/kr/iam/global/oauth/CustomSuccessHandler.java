package kr.iam.global.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.iam.domain.member.dto.CustomOAuth2User;
import kr.iam.global.jwt.JWTUtil;
import kr.iam.global.jwt.JwtEnum;
import kr.iam.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.jwt.expireAccessToken}")
    private static long EXPIRED_TIME;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, customUserDetails.getMemberId(), customUserDetails.getChannelId(), role, EXPIRED_TIME);
        cookieUtil.createCookie(JwtEnum.ACCESS_TOKEN_NAME.getDesc(), token, response);
//        cookieUtil.createCookie("memberId", customUserDetails.getMemberId().toString(), response);
//        cookieUtil.createCookie("channelId", customUserDetails.getChannelId().toString(), response);
        //response.sendRedirect("https://hzpodcaster.com/NewMyPage");
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/NewMyPage");
        //getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/login/oauth2/code/google");
    }
}