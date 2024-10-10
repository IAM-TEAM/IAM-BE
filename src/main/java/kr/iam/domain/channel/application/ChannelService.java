package kr.iam.domain.channel.application;

import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.domain.UsingCategory;
import kr.iam.domain.category.helper.CategoryHelper;
import kr.iam.domain.category.helper.UsingCategoryHelper;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.dto.req.ChannelSaveReqDto;
import kr.iam.domain.channel.dto.res.ChannelResDto;
import kr.iam.domain.channel.helper.ChannelHelper;
import kr.iam.domain.channel.mapper.ChannelMapper;
import kr.iam.domain.member.domain.Member;
import kr.iam.domain.member.helper.MemberHelper;
import kr.iam.global.aspect.member.MemberInfoParam;
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


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelHelper channelHelper;
    private final ChannelMapper channelMapper;
    private final MemberHelper memberHelper;
    private final CategoryHelper categoryHelper;
    private final UsingCategoryHelper usingCategoryHelper;
    private final S3UploadUtil s3UploadUtil;
    private final RssUtil rssUtil;
    private final CookieUtil cookieUtil;

    public Channel findByChannelId(Long channelId) {
        return channelHelper.findById(channelId);
    }

    @Transactional
    public Channel save() {
        Channel channel = Channel.builder().build();
        return channelHelper.save(channel);
    }

    /**
     * Channel, MemberInfo 저장 서비스
     * @param file
     * @param channelSaveReqDto
     * @param memberInfoParam
     * @throws IOException
     */
    @Transactional
    public void updateInfo(MultipartFile file, ChannelSaveReqDto channelSaveReqDto,
                           MemberInfoParam memberInfoParam) throws IOException {
        Long memberId = memberInfoParam.memberId();
        Long channelId = memberInfoParam.channelId();
        Channel channel = findByChannelId(channelId);
        List<String> mainCategories = new ArrayList<>();
        List<String> subCategories = new ArrayList<>();
        Map<String, List<String>> categories = channelSaveReqDto.channelCategories();
        for (String s : categories.keySet()) {
            mainCategories.add(s);
            subCategories.addAll(categories.get(s));
        }
        List<UsingCategory> usingCategories = new ArrayList<>();
        // 기존 UsingCategory 삭제
        usingCategoryHelper.deleteAllByChannel(channel);
        mainCategories.forEach(categoryName -> {
            Category category = categoryHelper.findByName(categoryName);
            usingCategories.add(UsingCategory.createUsingCategory(channel, category));
        });
        Member member = memberHelper.updateMember(memberId, channelSaveReqDto.username());
        String imageUrl = channel.getImage();
        if(file != null) {
            imageUrl = s3UploadUtil.saveProfileImage(file, memberId);
        }
        channel.updateChannel(channelSaveReqDto, usingCategories, imageUrl);

        String updated = rssUtil.updateRssFeed(member.getRssFeed(), channelSaveReqDto.channelTitle(), member.getRssFeed(),
                channelSaveReqDto.username(), channelSaveReqDto.channelDescription(), mainCategories, channelSaveReqDto.channelCategories(),
                member.getEmail(), imageUrl);
        s3UploadUtil.uploadRssFeed(member.getUsername(), updated);
    }

    /**
     * 유저 및 채널 정보 가져오기
     * @param memberInfoParam
     * @return
     */
    public ChannelResDto getInfo(MemberInfoParam memberInfoParam) {
        Long channelId = memberInfoParam.channelId();
        Channel channel = channelHelper.findAllInfoByChannelId(channelId);
        return channelMapper.toChannelResDto(channel, categoryHelper.getMemberCategory(channel.getMember().getRssFeed()));
    }
}
