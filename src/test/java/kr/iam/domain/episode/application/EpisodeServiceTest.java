package kr.iam.domain.episode.application;

import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.dao.EpisodeRepository;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode.dto.EpisodeDto;
import kr.iam.domain.episode_advertisement.application.EpisodeAdvertisementService;
import kr.iam.domain.member.application.MemberService;
import kr.iam.domain.member.domain.Member;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.RssUtil;
import kr.iam.global.util.S3UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static kr.iam.domain.episode.dto.EpisodeDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class EpisodeServiceTest {

    @Autowired
    EpisodeService episodeService;

    @Mock
    EpisodeRepository episodeRepository;

    @Mock
    MemberService memberService;

    @Mock
    ChannelService channelService;

    @Mock
    EpisodeAdvertisementService episodeAdvertisementService;

    @Mock
    CookieUtil cookieUtil;

    @Mock
    RssUtil rssUtil;

    @Mock
    S3UploadUtil s3UploadUtil;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void 세팅() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer test-token");
    }

    @Test
    void 에피소드_생성() throws IOException {
        //given
        Long memberId = 1L;
        Long channelId = 1L;
        EpisodeSaveRequestDto episodeSaveRequestDto = EpisodeSaveRequestDto.builder()
                .title("test").reservationTime(LocalDateTime.now()).build();
        MultipartFile multipartFile = null;
        String testUrl = "https://testUrl.com";
        Member member = Member.builder().id(memberId).build();
        Channel channel = Channel.builder().id(channelId).build();

        //when
        when(cookieUtil.getCookieValue("memberId", request)).thenReturn(String.valueOf(memberId));
        when(cookieUtil.getCookieValue("channelId", request)).thenReturn(String.valueOf(channelId));
        when(memberService.findById(memberId)).thenReturn(member);
        when(channelService.findByChannelId(channelId)).thenReturn(channel);
        when(s3UploadUtil.saveFile(null, episodeSaveRequestDto.getReservationTime(), memberId, "image"))
                .thenReturn(testUrl);
        when(s3UploadUtil.saveFile(null, episodeSaveRequestDto.getReservationTime(), memberId, "audio"))
                .thenReturn(testUrl);

        //then
        Episode episode = Episode.of(episodeSaveRequestDto, channel, testUrl, testUrl, episodeSaveRequestDto.getReservationTime());
        episodeRepository.save(episode);
    }

    @Test
    void 에피소드_리스트_조회() {

    }

    @Test
    void 에피소드_조회() {

    }

    @Test
    void 에피소드_삭제() {

    }
}