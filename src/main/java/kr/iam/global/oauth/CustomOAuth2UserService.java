package kr.iam.global.oauth;

import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.dao.ChannelRepository;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.member.dao.MemberRepository;
import kr.iam.domain.member.domain.Member;
import kr.iam.domain.member.domain.Role;
import kr.iam.domain.member.dto.CustomOAuth2User;
import kr.iam.domain.member.dto.MemberDTO;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.RssUtil;
import kr.iam.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final ChannelService channelService;
    private final RssUtil rssUtil;
    private final S3UploadUtil s3UploadUtil;

    @Override //유저 정보를 받음
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        //서비스 출처 가져옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //추후 작성
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        Member existData = memberRepository.findByUsername(username);
        MemberDTO userDTO;
        if(existData == null){
            Channel save = channelService.save();
            String rssFeed = rssUtil.createRssFeed();
            String saveRss = s3UploadUtil.uploadRssFeed(username, rssFeed);
            String updatedFeed = rssUtil.addAtomNamespaceAndFormat(rssFeed, saveRss);
            s3UploadUtil.uploadRssFeed(username, updatedFeed);
            Member userEntity = Member.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .image(oAuth2Response.getProfileImage())
                    .channel(save)
                    .rssFeed(saveRss)
                    .role(Role.ROLE_MEMBER)
                    .build();

            memberRepository.save(userEntity);
            userDTO = MemberDTO.builder()
                    .memberId(userEntity.getId())
                    .channelId(save.getId())
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_MEMBER)
                    .build();
        }
        else{
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            memberRepository.save(existData);
            userDTO = MemberDTO.builder()
                    .memberId(existData.getId())
                    .channelId(existData.getChannel().getId())
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_MEMBER)
                    .build();

        }
        return new CustomOAuth2User(userDTO);
    }
}