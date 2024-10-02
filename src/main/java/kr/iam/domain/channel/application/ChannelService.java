package kr.iam.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.channel.dao.ChannelRepository;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.detail_category.domain.DetailCategory;
import kr.iam.domain.member.application.MemberService;
import kr.iam.domain.member.domain.Member;
import kr.iam.domain.using_category.application.UsingCategoryService;
import kr.iam.domain.using_category.domain.UsingCategory;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.RssUtil;
import kr.iam.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static kr.iam.domain.channel.dto.ChannelDto.ChannelResponseDto;
import static kr.iam.domain.channel.dto.ChannelDto.ChannelSaveRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final UsingCategoryService usingCategoryService;
    private final S3UploadUtil s3UploadUtil;
    private final RssUtil rssUtil;
    private final CookieUtil cookieUtil;

    public boolean existsChannelById(Long channelId) {
        return channelRepository.existsById(channelId);
    }

    public Channel findByChannelId(Long channelId) {
        Optional<Channel> byId = channelRepository.findById(channelId);
        if (byId.isPresent()) {
            return byId.get();
        }else{
            throw new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND);
        }
    }

    @Transactional
    public Channel save() {
        Channel channel = Channel.builder().build();
        return channelRepository.save(channel);
    }

    /**
     * Channel, MemberInfo 저장 서비스
     * @param file
     * @param channelSaveRequestDto
     * @param request
     * @throws IOException
     */
    @Transactional
    public void updateInfo(MultipartFile file, ChannelSaveRequestDto channelSaveRequestDto,
                         HttpServletRequest request) throws IOException {
        Long memberId = Long.parseLong(cookieUtil.getCookieValue("memberId", request));
        Long channelId = Long.parseLong(cookieUtil.getCookieValue("channelId", request));
        Channel channel = findByChannelId(channelId);
        List<String> mainCategories = new ArrayList<>();
        List<String> subCategories = new ArrayList<>();
        Map<String, List<String>> categories = channelSaveRequestDto.getChannelCategories();
        for (String s : categories.keySet()) {
            mainCategories.add(s);
            subCategories.addAll(categories.get(s));
        }
        List<UsingCategory> usingCategories = new ArrayList<>();
        // 기존 UsingCategory 삭제
        usingCategoryService.deleteAllByChannel(channel);
        mainCategories.forEach(categoryName -> {
            Category category = categoryService.findByName(categoryName);
            usingCategories.add(UsingCategory.createUsingCategory(channel, category));
        });
        Member member = memberService.updateMember(memberId, channelSaveRequestDto.getUsername());
        String imageUrl = channel.getImage();
        if(file != null) {
            imageUrl = s3UploadUtil.saveProfileImage(file, memberId);
        }
        channel.updateChannel(channelSaveRequestDto, usingCategories, imageUrl);

        String updated = rssUtil.updateRssFeed(member.getRssFeed(), channelSaveRequestDto.getChannelTitle(), member.getRssFeed(),
                channelSaveRequestDto.getUsername(), channelSaveRequestDto.getChannelDescription(), mainCategories, subCategories,
                member.getEmail(), imageUrl);
        s3UploadUtil.uploadRssFeed(member.getUsername(), updated);
    }

    /**
     * 유저 및 채널 정보 가져오기
     * @param request
     * @return
     */
    public ChannelResponseDto getInfo(HttpServletRequest request) {
        Long channelId = Long.parseLong(cookieUtil.getCookieValue("channelId", request));
        Channel channel = channelRepository.findAllInfoByChannelId(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
        return ChannelResponseDto.from(channel, categoryService.getMemberCategory(channel.getMember().getRssFeed()));
    }
}
