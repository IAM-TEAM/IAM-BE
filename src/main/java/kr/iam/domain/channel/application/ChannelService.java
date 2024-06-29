package kr.iam.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.dto.CategoryDto;
import kr.iam.domain.channel.dao.ChannelRepository;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.dto.ChannelDto;
import kr.iam.domain.member.application.MemberService;
import kr.iam.domain.member.domain.Member;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static kr.iam.domain.category.dto.CategoryDto.*;
import static kr.iam.domain.channel.dto.ChannelDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
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
        String mainCategory = channelSaveRequestDto.getChannelCategory();
        String subCategory = channelSaveRequestDto.getChannelDetailCategory();
        Category byName = categoryService.findByName(mainCategory);
        Member member = memberService.updateMember(memberId, channelSaveRequestDto.getUsername());
        Channel channel = findByChannelId(channelId);
        String imageUrl = null;
        if(file != null) {
            imageUrl = s3UploadUtil.saveProfileImage(file, memberId);
        }
        channel.updateChannel(channelSaveRequestDto, byName, imageUrl);

        String updated = rssUtil.updateRssFeed(member.getRssFeed(), channelSaveRequestDto.getChannelTitle(), "Link",
                channelSaveRequestDto.getUsername(), channelSaveRequestDto.getChannelDescription(), subCategory,
                member.getEmail(), imageUrl);
        String result = s3UploadUtil.uploadRssFeed(member.getUsername(), updated);
        log.info("feed url = {}", result);
    }

    public ChannelResponseDto getInfo(HttpServletRequest request) {
        Long channelId = Long.parseLong(cookieUtil.getCookieValue("channelId", request));
        Channel channel = channelRepository.findAllInfoByChannelId(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
        return ChannelResponseDto.from(channel, categoryService.getMemberCategory(channel.getMember().getRssFeed()));
    }
}
