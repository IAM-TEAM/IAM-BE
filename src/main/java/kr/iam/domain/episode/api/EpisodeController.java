package kr.iam.domain.episode.api;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.modules.itunes.FeedInformation;
import com.rometools.modules.itunes.FeedInformationImpl;
import com.rometools.rome.feed.module.DCModuleImpl;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;
import kr.iam.domain.episode.application.S3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EpisodeController {

    private final S3Upload s3Upload;

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
        s3Upload.uploadRssFeed("test.xml", result);
        return ResponseEntity.ok().body(result);
    }
}