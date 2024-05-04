package kr.iam.domain.episode.application;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.dao.EpisodeRepository;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import kr.iam.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static kr.iam.domain.episode.dto.EpisodeDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final ChannelService channelService;
    private final S3UploadService s3UploadService;
    private final CookieUtil cookieUtil;

    public Long saveEpisode(MultipartFile image, MultipartFile content, EpisodeSaveRequestDto requestDto,
                              HttpServletRequest request) {
        try {
            Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
            Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
            Channel channel = channelService.findByChannelId(channelId);
            LocalDateTime uploadTime = requestDto.getReservationTime();
            if (uploadTime == null) {
                uploadTime = LocalDateTime.now();
            }
            String imageUrl = s3UploadService.saveFile(image, uploadTime, memberId, "image");
            String contentUrl = s3UploadService.saveFile(content, uploadTime, memberId, "audio");

            //RSS 피드 수정

            //DB 업로드
            Episode episode = Episode.of(requestDto, channel, imageUrl, contentUrl, uploadTime);
            episodeRepository.save(episode);
            return episode.getId();
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패");
        }
    }

    @Transactional
    public void delete(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.EPISODE_NOT_FOUND));
        s3UploadService.deleteFile(episode.getImage());
        s3UploadService.deleteFile(episode.getContent());
        episodeRepository.delete(episode);
    }
}
