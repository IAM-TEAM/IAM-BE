package kr.iam.domain.episode.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.modules.itunes.FeedInformationImpl;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.episode.application.EpisodeService;
import kr.iam.domain.episode.dto.req.EpisodeSaveReqDto;
import kr.iam.global.annotation.MemberInfo;
import kr.iam.global.aspect.member.MemberInfoParam;
import kr.iam.global.domain.SuccessResponse;
import kr.iam.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final S3UploadUtil s3UploadUtil;
    private final ObjectMapper objectMapper;
    private final EpisodeService episodeService;

    /**
     * 에피소드 생성
     * @param image
     * @param content
     * @param episodeData
     * @param memberInfoParam
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "에피소드 생성", description = "에피소드 생성(이미지, 오디오 파일 필수) RequestPart로 formData 형식")
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> uploadEpisode(@RequestPart(value = "image") MultipartFile image,
                                                            @RequestPart(value = "audio") MultipartFile content,
                                                            @RequestPart(value = "episodeData") String episodeData,
                                                            @MemberInfo MemberInfoParam memberInfoParam) throws JsonProcessingException {
        EpisodeSaveReqDto requestDto = objectMapper.readValue(episodeData, EpisodeSaveReqDto.class);
        return SuccessResponse.ok(episodeService.saveEpisode(image, content, requestDto, memberInfoParam));
    }

    /**
     * 에피소드 조회
     * @param episodeId
     * @return
     */
    @Operation(summary = "에피소드 조회", description = "에피소드 조회(Id를 PathVariable로 받음)")
    @GetMapping("/{episodeId}")
    public ResponseEntity<SuccessResponse<?>> getEpisodeInfo(@PathVariable("episodeId") Long episodeId) {
        return SuccessResponse.ok(episodeService.getEpisode(episodeId));
    }

    /**
     * 에피소드 리스트 조회
     * -1이면 임시 저장, 1이면 완료, 0이면 예정
     * @param upload
     * @param pageable
     * @param memberInfoParam
     * @return
     */
    @Operation(summary = "에피소드 리스트 조회", description = "upload가 1이면 완료 0이면 예정 -1이면 임시 저장")
    @GetMapping("/list/{upload}")
    public ResponseEntity<SuccessResponse<?>> getEpisodeList(@PathVariable("upload") int upload,
                                                                   @PageableDefault Pageable pageable,
                                                                   @MemberInfo MemberInfoParam memberInfoParam) {
        return SuccessResponse.ok(episodeService.getEpisodeList(upload, pageable, memberInfoParam));
    }

    /**
     * 에피소드 삭제
     * @param episodeId
     * @return
     */
    @Operation(summary = "에피소드 삭제", description = "PathVariable로 episodeId를 받아서 해당 에피소드 삭제 + RSS Feed도 같이 삭제")
    @DeleteMapping("/{episodeId}")
    public ResponseEntity<SuccessResponse<?>> deleteEpisode(@PathVariable("episodeId") Long episodeId, HttpServletRequest request) {
        episodeService.delete(episodeId, request);
        return SuccessResponse.ok(null);
    }

    @GetMapping(value = "/rssfeed", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getFeed() throws Exception {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("가천 팟캐스트 test");
        feed.setLink("https://podcasters.spotify.com/pod/show/qnr6ued8ig8");
        feed.setDescription("가천대학교 일상 라디오");

        // iTunes feed 정보 설정
        FeedInformationImpl feedInfo = new FeedInformationImpl();
        feedInfo.setImageUri("https://iam-test3.s3.ap-northeast-2.amazonaws.com/201935103_%EC%9D%B4%EC%98%81%EC%B0%AC.png");
        feedInfo.setAuthor("이영찬");
        feedInfo.setOwnerName("이영찬");
        feedInfo.setOwnerEmailAddress("lych0918@naver.com");
        feed.getModules().add(feedInfo);

        // 항목 설정
        List<SyndEntry> entries = new ArrayList<>();
        SyndEntry entry;
        SyndContent description;

        // 예제 항목
        entry = new SyndEntryImpl();
        entry.setTitle("test");
        entry.setLink("https://podcasters.spotify.com/pod/show/qnr6ued8ig8/episodes/test-e2j0qtl");
        entry.setPublishedDate(new Date());
        description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue("<p>test</p>");
        entry.setDescription(description);

        // iTunes 항목 정보 설정
        EntryInformation entryInfo = new EntryInformationImpl();
        entryInfo.setAuthor("이영찬");
        entry.getModules().add(entryInfo);

        entries.add(entry);
        feed.setEntries(entries);

        // RSS 피드 출력
        String result = new SyndFeedOutput().outputString(feed);
        s3UploadUtil.uploadRssFeed("test.xml", result);
        return ResponseEntity.ok().body(result);
    }
}