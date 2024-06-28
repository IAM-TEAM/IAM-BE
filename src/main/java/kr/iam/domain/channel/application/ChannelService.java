package kr.iam.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.channel.dao.ChannelRepository;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.dto.ChannelDto;
import kr.iam.domain.member.application.MemberService;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
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

    @Transactional
    public void updateInfo(MultipartFile file, ChannelSaveRequestDto channelSaveRequestDto,
                         HttpServletRequest request) throws IOException {
        Long memberId = Long.parseLong(request.getParameter("memberId"));
        Long channelId = Long.parseLong(request.getParameter("channelId"));
        String mainCategory = channelSaveRequestDto.getChannelCategory();
        String subCategory = channelSaveRequestDto.getChannelDetailCategory();
        Category byName = categoryService.findByName(mainCategory);
        memberService.updateMember(memberId, channelSaveRequestDto.getUsername());
        Channel channel = findByChannelId(channelId);
        channel.updateChannel(channelSaveRequestDto, byName);

        String imageUrl = s3UploadUtil.saveFile(file, LocalDateTime.now(), memberId);


    }
}
