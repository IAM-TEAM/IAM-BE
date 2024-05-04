package kr.iam.global.util;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.rome.feed.rss.Image;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class RssUtil {

    public void addEpisode(String feedUrl, SyndEntry newEpisode) throws IOException, FeedException {
        // 기존 피드 파일을 읽기
        String filePath = "updated_feed.xml";
//        URL url = new URL(feedUrl);
//        XmlReader reader = new XmlReader(url);
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        XmlReader reader = new XmlReader(inputStream);
        SyndFeed feed = new SyndFeedInput().build(reader);

        // 기존 피드에 새로운 항목 추가
        feed.getEntries().add(0, newEpisode);  // Add at the beginning of the list

        // 파일에 새로운 피드를 덮어쓰기로 출력
        FileWriter writer = new FileWriter(file);  // 기존 파일 경로를 사용하여 파일을 덮어쓰기
        SyndFeedOutput output = new SyndFeedOutput();
        output.output(feed, writer);
        writer.close();  // 리소스 정리
        reader.close();  // XML Reader 닫기
    }

    public SyndEntry createNewEpisode(String title, String description, String link, LocalDateTime pubDate,
                                      String enclosureUrl, String imageUrl, String type, String creator) {
        Date date = Date.from(pubDate.atZone(ZoneId.systemDefault()).toInstant());
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(title);
        entry.setLink(link);
        entry.setPublishedDate(date);

        Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1");
        Element creatorElement = new Element("creator", dcNamespace);
        creatorElement.setText(creator); // 작성자 정보를 설정합니다.
        entry.getForeignMarkup().add(creatorElement);

        SyndContent content = new SyndContentImpl();
        content.setType("text/html");
        content.setValue(description);
        entry.setDescription(content);

        SyndEnclosure enclosure = new SyndEnclosureImpl();
        enclosure.setUrl(enclosureUrl);
        enclosure.setLength(getFileSize(enclosureUrl));
        enclosure.setType(type);
        entry.getEnclosures().add(enclosure);

        // iTunes image 태그 추가
        EntryInformation itunesInfo = new EntryInformationImpl();
        itunesInfo.setImageUri(imageUrl);
        itunesInfo.setDuration(null);
        itunesInfo.setExplicit(false);
        itunesInfo.setEpisodeType("full");
        itunesInfo.setSummary("description");
        entry.getModules().add(itunesInfo);

        Element guid = new Element("guid");
        guid.setText(link);
        guid.setAttribute("isPermaLink", "false");
        entry.getForeignMarkup().add(guid);
        return entry;
    }

    private long getFileSize(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            // 'HEAD' 요청을 사용하면 파일의 내용을 다운로드하지 않고 헤더 정보만 가져올 수 있습니다.
            return connection.getContentLengthLong(); // 파일 크기를 반환합니다.
        } catch (Exception e) {
            System.err.println("Error getting file size: " + e.getMessage());
            return -1;
        }
    }
}
