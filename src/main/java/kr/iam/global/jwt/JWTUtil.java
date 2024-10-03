package kr.iam.global.jwt;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${spring.jwt.expireAccessToken}")
    private long expiredAt;
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getMemberName(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(JwtEnum.MEMBER_NAME.getDesc(), String.class);
    }

    public Long getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(JwtEnum.MEMBER_ID.getDesc(), Long.class);
    }

    public Long getChannelId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(JwtEnum.CHANNEL_ID.getDesc(), Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(JwtEnum.MEMBER_ROLE.getDesc(), String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String memberName, Long memberId, Long channelId, String memberRole, Long expiredMs) {
        return Jwts.builder()
                .claim(JwtEnum.MEMBER_NAME.getDesc(), memberName)
                .claim(JwtEnum.MEMBER_ID.getDesc(), memberId)
                .claim(JwtEnum.CHANNEL_ID.getDesc(), channelId)
                .claim(JwtEnum.MEMBER_ROLE.getDesc(), memberRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredAt))
                .signWith(secretKey)
                .compact();
    }
}