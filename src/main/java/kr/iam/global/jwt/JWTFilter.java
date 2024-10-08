package kr.iam.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.iam.domain.member.domain.Role;
import kr.iam.domain.member.dto.CustomOAuth2User;
import kr.iam.domain.member.dto.MemberDTO;
import kr.iam.global.util.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;
    @Value("${spring.jwt.expireAccessToken}")
    private long expiredTime;

    public JWTFilter(JWTUtil jwtUtil, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = cookieUtil.getCookieValue(JwtEnum.ACCESS_TOKEN_NAME.getDesc(), request);

        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        try {
            // 토큰에서 username과 role 획득
            String memberName = jwtUtil.getMemberName(token);
            Long memberId = jwtUtil.getMemberId(token);
            Long channelId = jwtUtil.getChannelId(token);
            String role = jwtUtil.getRole(token);
            //userDTO를 생성하여 값 set
            MemberDTO userDTO = MemberDTO.of(memberName, memberId, channelId, Role.valueOf(role));

            //UserDetails에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("토큰이 유효합니다.");

        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다.");

            // 토큰이 만료되었을 때 재발급 로직 추가
            try {
                // 만료된 토큰에서 username과 role을 추출
                String memberName = e.getClaims().get(JwtEnum.MEMBER_NAME.getDesc(), String.class);
                Long memberId = e.getClaims().get(JwtEnum.MEMBER_ID.getDesc(), Long.class);
                Long channelId = e.getClaims().get(JwtEnum.CHANNEL_ID.getDesc(), Long.class);
                String role = e.getClaims().get(JwtEnum.MEMBER_ROLE.getDesc(), String.class);

                // 새로운 토큰 재발급
                String newToken = jwtUtil.createJwt(memberName, memberId, channelId, role, expiredTime); // 새로운 토큰 재발급 (1시간 유효기간)

                // 새로운 토큰을 쿠키에 추가
                cookieUtil.createCookie(JwtEnum.ACCESS_TOKEN_NAME.getDesc(), newToken, response);

                // 재발급된 토큰을 사용하여 인증 정보 설정
                token = newToken;

                //userDTO를 생성하여 값 set
                MemberDTO userDTO = MemberDTO.of(memberName, memberId, channelId, Role.valueOf(role));

                //UserDetails에 회원 정보 객체 담기
                CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

                //스프링 시큐리티 인증 토큰 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
                //세션에 사용자 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("토큰이 재발급되었습니다.");
            } catch (Exception ex) {
                System.out.println("토큰 재발급 중 오류 발생: " + ex.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation error");
                return;
            }
        } catch (Exception e) {
            System.out.println("토큰 검증 중 오류 발생: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation error");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
