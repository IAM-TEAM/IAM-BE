package kr.iam.global.aspect.member;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.global.annotation.MemberInfo;
import kr.iam.global.error.code.GlobalErrorCode;
import kr.iam.global.error.exception.UnauthorizedException;
import kr.iam.global.jwt.JWTUtil;
import kr.iam.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static kr.iam.global.jwt.JwtEnum.ACCESS_TOKEN_NAME;

@RequiredArgsConstructor
@Component
public class MemberInfoParamHandlerArgumentResolver implements HandlerMethodArgumentResolver {
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberInfoParam.class)
                && parameter.hasParameterAnnotation(MemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = cookieUtil.getCookieValue(ACCESS_TOKEN_NAME.getDesc(), request);
        if(!StringUtils.hasText(accessToken)){
            throw new UnauthorizedException(GlobalErrorCode.INVALID_TOKEN);
        }
        Long memberId = jwtUtil.getMemberId(accessToken);
        Long channelId = jwtUtil.getChannelId(accessToken);
//        String memberRole = jwtUtil.getMemberRole(accessToken);
        return MemberInfoParam.of(memberId, channelId);
    }
}
