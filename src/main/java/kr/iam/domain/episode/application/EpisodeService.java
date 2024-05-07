package kr.iam.domain.episode.application;

import com.amazonaws.services.cloudformation.model.transform.ListTypesResultStaxUnmarshaller;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.advertisement.application.AdvertisementService;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.dao.EpisodeRepository;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode_advertisement.application.EpisodeAdvertisementService;
import kr.iam.domain.episode_advertisement.domain.EpisodeAdvertisement;
import kr.iam.domain.member.application.MemberService;
import kr.iam.domain.member.domain.Member;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import kr.iam.global.util.CookieUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static kr.iam.domain.episode.dto.EpisodeDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final ChannelService channelService;
    private final MemberService memberService;
    private final S3UploadUtil s3UploadUtil;
    private final CookieUtil cookieUtil;
    private final RssUtil rssUtil;
    private final AdvertisementService advertisementService;

    /**
     * 에피소드 저장
     * @param image
     * @param content
     * @param requestDto
     * @param request
     * @return
     */
    @Transactional
    public Long saveEpisode(MultipartFile image, MultipartFile content, EpisodeSaveRequestDto requestDto,
                              HttpServletRequest request) {
        try {
            Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
            Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
            Member member = memberService.findById(memberId);
            Channel channel = channelService.findByChannelId(channelId);
            LocalDateTime uploadTime = requestDto.getReservationTime();
            if (uploadTime == null) {
                uploadTime = LocalDateTime.now();
            }
            String imageUrl = s3UploadUtil.saveFile(image, uploadTime, memberId, "image");
            String contentUrl = s3UploadUtil.saveFile(content, uploadTime, memberId, "audio");

            //RSS 피드 수정
            SyndEntry newEpisode = rssUtil.createNewEpisode(requestDto.getTitle(), requestDto.getDescription(),
                    "https://test.test.iam/member0/episodeId.e1e2e23r", uploadTime,
                    contentUrl, imageUrl, "audio/mpeg", member.getUsername());
            rssUtil.addEpisode("https://anchor.fm/s/f5858a40/podcast/rss", newEpisode);

            //DB 업로드
            Episode episode = Episode.of(requestDto, channel, imageUrl, contentUrl, uploadTime);
            if (requestDto.getAdvertiseId() != null) {
                List<EpisodeAdvertisement> episodeAdvertisementList =
                        toEpisodeAdvertisementList(episode, requestDto.getAdvertiseId(), requestDto.getAdvertiseStart());
                episode.getEpisodeAdvertisementList().addAll(episodeAdvertisementList);
            }
            episodeRepository.save(episode);

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
    public EpisodeInfoResponseDto getEpisode(Long episodeId) {
        Episode episode = episodeRepository.findByIdAndEpisodeAdvertisement(episodeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.EPISODE_NOT_FOUND));
        return EpisodeInfoResponseDto.of(episode);
    }

    /**
     * 에피소드 리스트 조회(페이징)
     * @param upload
     * @param pageable
     * @param request
     * @return
     */
    public Page<EpisodeListInfoDto> getEpisodeList(int upload, Pageable pageable, HttpServletRequest request) {
        Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
        Channel channel = channelService.findByChannelId(channelId);
        Page<Episode> byUploadAndChannel = episodeRepository.findByUploadAndChannel(upload, channel, pageable);
        List<EpisodeListInfoDto> dtos = byUploadAndChannel.getContent().stream()
                .map(EpisodeListInfoDto::of)
                .toList();
        return new PageImpl<>(dtos, pageable, byUploadAndChannel.getTotalElements());
    }

    /**
     * 에피소드 삭제
     * @param episodeId
     */
    @Transactional
    public void delete(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.EPISODE_NOT_FOUND));
        s3UploadUtil.deleteFile(episode.getImage());
        s3UploadUtil.deleteFile(episode.getContent());
        try {
            rssUtil.deleteEpisode("test", "test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
        episodeRepository.delete(episode);
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
                    Advertisement byAdvertiseId = advertisementService.findByAdvertiseId(Long.valueOf(adIds.get(i)));
                    return EpisodeAdvertisement.of(episode, byAdvertiseId, startTimeList.get(i));
                })
                .collect(Collectors.toList());
        return result;
    }
}
