package kr.iam.domain.episode.application;

import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.dao.EpisodeRepository;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.member.application.MemberService;
import kr.iam.domain.member.domain.Member;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.RssUtil;
import kr.iam.global.util.S3UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kr.iam.domain.episode.dto.EpisodeDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class EpisodeServiceTest {

    @Autowired
    EpisodeService episodeService;

    @MockBean
    EpisodeRepository episodeRepository;

    @MockBean
    MemberService memberService;

    @MockBean
    ChannelService channelService;

    @MockBean
    EpisodeAdvertisementService episodeAdvertisementService;

    @MockBean
    CookieUtil cookieUtil;

    @MockBean
    RssUtil rssUtil;

    @MockBean
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
                .title("test").description("test").age(true).reservationTime(LocalDateTime.now()).build();
        MultipartFile multipartFile = null;
        String testUrl = "https://testUrl.com";
        Member member = Member.builder().id(memberId).build();
        Channel channel = Channel.builder().id(channelId).build();

        when(cookieUtil.getCookieValue("memberId", request)).thenReturn(String.valueOf(memberId));
        when(cookieUtil.getCookieValue("channelId", request)).thenReturn(String.valueOf(channelId));
        when(memberService.findById(memberId)).thenReturn(member);
        when(channelService.findByChannelId(channelId)).thenReturn(channel);
        when(s3UploadUtil.saveFile(null, episodeSaveRequestDto.getReservationTime(), memberId))
                .thenReturn(testUrl);
        when(s3UploadUtil.saveFile(null, episodeSaveRequestDto.getReservationTime(), memberId))
                .thenReturn(testUrl);

        //when
        Long id = episodeService.saveEpisode(multipartFile, multipartFile, episodeSaveRequestDto, request);

        //then
        verify(episodeRepository, times(1)).save(any(Episode.class));
        assertNotNull(id);
    }

    @Test
    void 에피소드_조회() {
        //given
        Long episodeId = 1L;
        Episode episode = Episode.builder().id(1L).title("testTitle").build();
        when(episodeRepository.findByIdAndEpisodeAdvertisement(episodeId))
                .thenReturn(Optional.ofNullable(episode));

        //when
        EpisodeInfoResponseDto testEpisode = episodeService.getEpisode(episodeId);

        //then
        assertThat(testEpisode.getTitle()).isEqualTo("testTitle");
    }

    @Test
    void 에피소드_리스트_조회() {
        //given
        int upload = 1;
        Long channelId = 1L;
        Channel channel = Channel.builder().id(channelId).build();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Episode episode = new Episode();
        List<Episode> episodes = List.of(episode);
        Page<Episode> page = new PageImpl<>(episodes, pageRequest, 1);
        when(cookieUtil.getCookieValue("channelId", request))
                .thenReturn(String.valueOf(channelId));
        when(channelService.findByChannelId(channelId))
                .thenReturn(channel);
        when(episodeRepository.findByUploadAndChannel(upload, channel, pageRequest)).thenReturn(page);

        //when
        Page<EpisodeListInfoDto> result = episodeService.getEpisodeList(upload, pageRequest, request);

        //then
        assertNotNull(result);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void 에피소드_삭제() throws IOException {
        //given
        Long episodeId = 1L;
        Long memberId = 1L;
        String feedUrl = "test";
        Episode episode = new Episode();
        Member member = Member.builder().id(memberId).rssFeed(feedUrl).build();
        when(memberService.findById(memberId)).thenReturn(member);
        when(cookieUtil.getCookieValue("memberId", request)).thenReturn(String.valueOf(memberId));
        when(episodeRepository.findById(episodeId))
                .thenReturn(Optional.of(episode));
        when(s3UploadUtil.saveFile(null, LocalDateTime.now(), memberId))
                .thenReturn(null);

        //when
        episodeService.delete(episodeId, request);

        //then
        verify(episodeRepository, times(1)).delete(any(Episode.class));
    }
}