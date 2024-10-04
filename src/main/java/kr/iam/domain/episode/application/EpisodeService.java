package kr.iam.domain.episode.application;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.advertisement.helper.AdvertisementHelper;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.helper.ChannelHelper;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode.domain.EpisodeAdvertisement;
import kr.iam.domain.episode.dto.info.EpisodeInfo;
import kr.iam.domain.episode.dto.req.EpisodeSaveReqDto;
import kr.iam.domain.episode.dto.res.EpisodeInfoResDto;
import kr.iam.domain.episode.dto.res.EpisodePageResDto;
import kr.iam.domain.episode.helper.EpisodeHelper;
import kr.iam.domain.episode.mapper.EpisodeMapper;
import kr.iam.domain.member.domain.Member;
import kr.iam.domain.member.helper.MemberHelper;
import kr.iam.global.aspect.member.MemberInfoParam;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.LinkUtil;
import kr.iam.global.util.RssUtil;
import kr.iam.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpisodeService {

    private final EpisodeHelper episodeHelper;
    private final EpisodeMapper episodeMapper;
    private final ChannelHelper channelHelper;
    private final MemberHelper memberHelper;
    private final AdvertisementHelper advertisementHelper;
    private final S3UploadUtil s3UploadUtil;
    private final CookieUtil cookieUtil;
    private final RssUtil rssUtil;

    /**
     * 에피소드 저장
     * @param image
     * @param content
     * @param requestDto
     * @param memberInfoParam
     * @return
     */
    @Transactional
    public Long saveEpisode(MultipartFile image, MultipartFile content, EpisodeSaveReqDto requestDto,
                            MemberInfoParam memberInfoParam) {
        try {
            Long channelId = memberInfoParam.channelId();
            Long memberId = memberInfoParam.memberId();
            Member member = memberHelper.findById(memberId);
            Channel channel = channelHelper.findById(channelId);
            LocalDateTime uploadTime = requestDto.reservationTime();
            if (uploadTime == null) {
                uploadTime = LocalDateTime.now();
            }
            String imageUrl = s3UploadUtil.saveFile(image, uploadTime, memberId);
            String contentUrl = s3UploadUtil.saveFile(content, uploadTime, memberId);

            //DB 업로드
            Episode episode = Episode.of(requestDto, channel, imageUrl, contentUrl, uploadTime);
            if (requestDto.advertiseId() != null) {
                List<EpisodeAdvertisement> episodeAdvertisementList =
                        toEpisodeAdvertisementList(episode, requestDto.advertiseId(), requestDto.advertiseStart());
                episode.getEpisodeAdvertisementList().addAll(episodeAdvertisementList);
            }
            episodeHelper.save(episode);

            //RSS 피드 수정
            String link = makeEpisodeLink(episode.getId());
            SyndEntry newEpisode = rssUtil.createNewEpisode(requestDto.title(), requestDto.description(),
                    link, uploadTime, contentUrl, imageUrl, "audio/mpeg", member.getName());
            rssUtil.addEpisode(member.getRssFeed(), newEpisode);

            return episode.getId();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 실패");
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 에피소드 조회
     * @param episodeId
     * @return
     */
    public EpisodeInfoResDto getEpisode(Long episodeId) {
        Episode episode = episodeHelper.findByIdAndEpisodeAdvertisement(episodeId);
        return episodeMapper.toEpisodeInfoResDto(episode);
    }

    /**
     * 에피소드 리스트 조회(페이징)
     * @param upload
     * @param pageable
     * @param memberInfoParam
     * @return
     */
    public EpisodePageResDto getEpisodeList(int upload, Pageable pageable, MemberInfoParam memberInfoParam) {
        Long channelId = memberInfoParam.channelId();
        Channel channel = channelHelper.findById(channelId);
        Page<Episode> byUploadAndChannel = episodeHelper.findByUploadAndChannel(upload, channel, pageable);
        List<EpisodeInfo> dtos = byUploadAndChannel.getContent().stream()
                .map(EpisodeInfo::of)
                .toList();
        PageImpl<EpisodeInfo> episodeInfos = new PageImpl<>(dtos, pageable, byUploadAndChannel.getTotalElements());
        return episodeMapper.toEpisodePageResDto(episodeInfos);
    }

    /**
     * 에피소드 삭제
     * 링크는 아마 https://iam.domain/member + memberId/EpisodeId 요렇게 될듯?
     * ex) https://iam.domain/member33/epsisode452
     * @param episodeId
     */
    @Transactional
    public void delete(Long episodeId, HttpServletRequest request) {
        Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
        Member member = memberHelper.findById(memberId);
        Episode episode = episodeHelper.findById(episodeId);
        s3UploadUtil.deleteFile(episode.getImage());
        s3UploadUtil.deleteFile(episode.getContent());
        String link = makeEpisodeLink(episodeId);
        try {
            rssUtil.deleteEpisode(member.getRssFeed(), link);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
        episodeHelper.delete(episode);
    }

    private List<String> toList(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private List<EpisodeAdvertisement> toEpisodeAdvertisementList(Episode episode, String ads, String times) {
        List<String> adIds = toList(ads);

        List<String> startTimeList = toList(times);
        List<EpisodeAdvertisement> result = IntStream.range(0, adIds.size())
                .mapToObj(i -> {
                    Advertisement byAdvertiseId = advertisementHelper.findByAdvertiseId(Long.valueOf(adIds.get(i)));
                    return EpisodeAdvertisement.of(episode, byAdvertiseId, startTimeList.get(i));
                })
                .collect(Collectors.toList());
        return result;
    }

    private String makeEpisodeLink(Long episodeId) {
        return LinkUtil.LINK.getLink() + "/" + LinkUtil.EPISODIC.getLink() + "/" + episodeId;
    }
}
