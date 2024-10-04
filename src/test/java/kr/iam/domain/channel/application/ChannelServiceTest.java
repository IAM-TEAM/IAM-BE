//package kr.iam.domain.channel.application;
//
//import jakarta.servlet.http.Cookie;
//import kr.iam.domain.category.application.CategoryService;
//import kr.iam.domain.category.domain.Category;
//import kr.iam.domain.channel.dao.ChannelRepository;
//import kr.iam.domain.channel.domain.Channel;
//import kr.iam.domain.channel.dto.ChannelDto;
//import kr.iam.domain.category.domain.DetailCategory;
//import kr.iam.domain.member.application.MemberService;
//import kr.iam.domain.member.domain.Member;
//import kr.iam.global.util.CookieUtil;
//import kr.iam.global.util.RssUtil;
//import kr.iam.global.util.S3UploadUtil;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static kr.iam.domain.channel.dto.ChannelDto.*;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@Transactional
//class ChannelServiceTest {
//
//    @Autowired
//    ChannelService channelService;
//
//    @MockBean
//    ChannelRepository channelRepository;
//
//    @MockBean
//    MemberService memberService;
//
//    @MockBean
//    CategoryService categoryService;
//
//    @MockBean
//    S3UploadUtil s3UploadUtil;
//
//    @MockBean
//    RssUtil rssUtil;
//
//    @MockBean
//    CookieUtil cookieUtil;
//
//    MockHttpServletRequest request = new MockHttpServletRequest();
//
//    @Test
//    void 채널Id로_채널_찾기() {
//        //given
//        Long channelId = 1L;
//        Channel channel = Channel.builder().id(1L).build();
//        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
//
//        //when
//        Channel result = channelService.findByChannelId(channelId);
//
//        //then
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(channelId);
//    }
//
//    @Test
//    void 채널_저장() {
//        //given
//        Long channelId = 1L;
//        Channel channel = Channel.builder().id(1L).build();
//        when(channelRepository.save(any(Channel.class))).thenReturn(channel);
//
//        //when
//        Channel save = channelService.save();
//
//        //then
//        assertThat(save).isNotNull();
//        assertThat(save.getId()).isEqualTo(channelId);
//    }
//
//    @Test
//    void 채널_정보_업데이트_file_Not_Null() throws IOException {
//        //given
//        MultipartFile file = Mockito.mock(MultipartFile.class);
//        String imageUrl = "testImage";
//        String rssUrl = "testUrl";
//        ChannelSaveRequestDto channelSaveRequestDto = ChannelSaveRequestDto.builder()
//                .username("testUsername")
//                .channelTitle("testTitle")
//                .channelDescription("testDescription")
//                .channelCategory("testCategory")
//                .channelDetailCategory("testDetailCategory")
//                .ageLimit(false)
//                .build();
//        Long memberId = 1L;
//        Long channelId = 1L;
//        DetailCategory detailCategory = DetailCategory.builder().name("testDetailCategory2").build();
//        Category category = Category.builder().name("testCategory2").detailCategories(List.of(detailCategory)).build();
//        Member member = Member.builder().id(memberId).username("testUsername2").build();
//        Channel channel = Channel.builder().id(channelId).image("testImage2").title("testTitle2")
//                .description("testDescription2").category(category).build();
//        when(cookieUtil.getCookieValue("memberId", request)).thenReturn(String.valueOf(memberId));
//        when(cookieUtil.getCookieValue("channelId", request)).thenReturn(String.valueOf(channelId));
//        when(categoryService.findByName(channelSaveRequestDto.getChannelCategory())).thenReturn(category);
//        when(memberService.updateMember(memberId, channelSaveRequestDto.getUsername())).thenReturn(member);
//        when(channelRepository.findById(anyLong())).thenReturn(Optional.of(channel));
//        when(s3UploadUtil.saveProfileImage(file, memberId)).thenReturn(imageUrl);
//        when(rssUtil.updateRssFeed(member.getRssFeed(), channelSaveRequestDto.getChannelTitle(), "TestLink",
//                channelSaveRequestDto.getUsername(), channelSaveRequestDto.getChannelDescription(), "testCategory",
//                member.getEmail(), imageUrl)).thenReturn(rssUrl);
//        when(s3UploadUtil.uploadRssFeed("testName", rssUrl)).thenReturn("ok");
//
//        //when
//        channelService.updateInfo(file, channelSaveRequestDto, request);
//
//        //then
//        assertThat(channel.getImage()).isEqualTo("testImage");
//        assertThat(channel.getTitle()).isEqualTo("testTitle");
//        assertThat(channel.getDescription()).isEqualTo("testDescription");
//    }
//
//    @Test
//    void 채널_정보_업데이트_File_Null() throws IOException {
//        //given
//        MultipartFile file = null;
//        String imageUrl = "testUrl";
//        String rssUrl = "testUrl";
//        ChannelSaveRequestDto channelSaveRequestDto = ChannelSaveRequestDto.builder()
//                .username("testUsername")
//                .channelTitle("testTitle")
//                .channelDescription("testDescription")
//                .channelCategory("testCategory")
//                .channelDetailCategory("testDetailCategory")
//                .ageLimit(false)
//                .build();
//        Long memberId = 1L;
//        Long channelId = 1L;
//        DetailCategory detailCategory = DetailCategory.builder().name("testDetailCategory2").build();
//        Category category = Category.builder().name("testCategory2").detailCategories(List.of(detailCategory)).build();
//        Member member = Member.builder().id(memberId).username("testUsername2").build();
//        Channel channel = Channel.builder().id(channelId).image("testImage2").title("testTitle2").description("testDescription2")
//                .category(category).build();
//        when(cookieUtil.getCookieValue("memberId", request)).thenReturn(String.valueOf(memberId));
//        when(cookieUtil.getCookieValue("channelId", request)).thenReturn(String.valueOf(channelId));
//        when(categoryService.findByName(channelSaveRequestDto.getChannelCategory())).thenReturn(category);
//        when(memberService.updateMember(memberId, channelSaveRequestDto.getUsername())).thenReturn(member);
//        when(channelRepository.findById(anyLong())).thenReturn(Optional.of(channel));
//        when(s3UploadUtil.saveProfileImage(file, memberId)).thenReturn(imageUrl);
//        when(rssUtil.updateRssFeed(member.getRssFeed(), channelSaveRequestDto.getChannelTitle(), "TestLink",
//                channelSaveRequestDto.getUsername(), channelSaveRequestDto.getChannelDescription(), "testCategory",
//                member.getEmail(), imageUrl)).thenReturn(rssUrl);
//        when(s3UploadUtil.uploadRssFeed("testName", rssUrl)).thenReturn("ok");
//
//        //when
//        channelService.updateInfo(file, channelSaveRequestDto, request);
//
//        //then
//        assertThat(channel.getImage()).isEqualTo("testImage2");
//        assertThat(channel.getTitle()).isEqualTo("testTitle");
//        assertThat(channel.getDescription()).isEqualTo("testDescription");
//    }
//
//    @Test
//    void 채널_정보_가져오기() {
//        //given
//        Long channelId = 1L;
//        String rssFeedUrl = "testUrl";
//        DetailCategory detailCategory = DetailCategory.builder().name("testDetailCategory2").build();
//        Category category = Category.builder().name("testCategory2").detailCategories(List.of(detailCategory)).build();
//        Member member = Member.builder().id(1L).name("testUsername2").rssFeed(rssFeedUrl).build();
//        Channel channel = Channel.builder().id(channelId).image("testImage2").title("testTitle2").description("testDescription2").age(false)
//                .member(member).build();
//        when(cookieUtil.getCookieValue("channelId", request)).thenReturn(String.valueOf(channelId));
//        when(channelRepository.findAllInfoByChannelId(channelId)).thenReturn(Optional.of(channel));
//        when(categoryService.getMemberCategory(rssFeedUrl)).thenReturn(Collections.singletonList(detailCategory.getName()));
//
//        //when
//        ChannelResponseDto info = channelService.getInfo(request);
//
//        //then
//        assertThat(info).isNotNull();
//        assertThat(info.getUsername()).isEqualTo(member.getName());
//        assertThat(info.getChannelCategories()).isEqualTo(category.getName());
//        assertThat(info.getChannelTitle()).isEqualTo("testTitle2");
//        assertThat(info.getChannelDescription()).isEqualTo("testDescription2");
//        assertThat(info.getChannelDetailCategories()).isEqualTo(detailCategory.getName());
//        assertThat(info.getAgeLimit()).isFalse();
//    }
//}