package kr.iam.domain.episode.application;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.global.config.RestTemplateConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final RestTemplateConfig restTemplate;

    private void parseRssFeed(String rssFeed) {
        try {
            Document document = Jsoup.connect(rssFeed).get();
            Elements items = document.select("item");

            for (Element item : items) {
                String title = item.select("item").text();
                String description = item.select("description").text();
                String audioUrl = item.select("enclosure[url]").attr("url");

            }
        } catch (IOException e) {
            throw new RuntimeException("Rss Feed 파싱 에러");
        }
    }
}
