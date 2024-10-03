package kr.iam.global.config;

import kr.iam.global.aspect.member.MemberInfoParamHandlerArgumentResolver;
import kr.iam.global.jwt.JWTUtil;
import kr.iam.global.util.CookieUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public WebConfig(JWTUtil jwtUtil, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberInfoParamHandlerArgumentResolver(jwtUtil, cookieUtil));
    }
}
