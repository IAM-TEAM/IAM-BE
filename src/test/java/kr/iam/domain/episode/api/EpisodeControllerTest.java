package kr.iam.domain.episode.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.advertisement.application.AdvertisementService;
import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.episode.application.EpisodeService;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.RssUtil;
import kr.iam.global.util.S3UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static kr.iam.domain.episode.dto.EpisodeDto.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EpisodeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class EpisodeControllerTest {

    @MockBean
    EpisodeService episodeService;

    @MockBean
    S3UploadUtil s3UploadUtil;

    @MockBean
    CookieUtil cookieUtil;

    @MockBean
    RssUtil rssUtil;

    @MockBean
    ChannelService channelService;

    @MockBean
    AdvertisementService advertisementService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockMultipartHttpServletRequestBuilder requestBuilder;

    Long memberId;
    Long channelId;

    @BeforeEach
    void 세팅() {
        requestBuilder = (MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart("/episode")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.MULTIPART_FORM_DATA);
        memberId = 1L;
        channelId = 2L;
    }

    @Test
    @WithMockUser
    void 에피소드_생성() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", (byte[]) null);
        MockMultipartFile audio = new MockMultipartFile("audio", "audio.mp3", "audio/mpeg", (byte[]) null);
        String episodeData = "{\"title\":\"졸리당\", \"description\":\"응애\", \"advertiseStart\":\"1:00, 3:00\", \"advertiseId\":\"1, 2\", \"age\":true, \"upload\":0}";
        MockMultipartFile episodeDataFile = new MockMultipartFile("episodeData", "", "application/json", episodeData.getBytes());

        given(episodeService.saveEpisode(any(MultipartFile.class), any(MultipartFile.class), any(EpisodeSaveRequestDto.class), any(HttpServletRequest.class)))
                .willReturn(2L);

        // when, then
        mockMvc.perform(requestBuilder
                        .file(image)
                        .file(audio)
                        .file(episodeDataFile)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("2 created"));

        verify(episodeService, times(1))
                .saveEpisode(
                        any(MultipartFile.class),
                        any(MultipartFile.class),
                        any(EpisodeSaveRequestDto.class),
                        any(HttpServletRequest.class)
                );
    }
}