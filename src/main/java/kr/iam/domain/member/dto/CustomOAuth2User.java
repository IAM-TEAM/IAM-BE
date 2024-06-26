package kr.iam.domain.member.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDTO memberDTO;

    public CustomOAuth2User(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    //사용 x
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return memberDTO.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return memberDTO.getName();
    }

    public String getUsername() {

        return memberDTO.getUsername();
    }

    public Long getMemberId() {
        return memberDTO.getMemberId();
    }

    public Long getChannelId() {
        return memberDTO.getChannelId();
    }
}
