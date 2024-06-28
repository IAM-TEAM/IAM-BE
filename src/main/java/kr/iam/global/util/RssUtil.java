package kr.iam.global.util;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.modules.itunes.FeedInformation;
import com.rometools.modules.itunes.FeedInformationImpl;
import com.rometools.modules.itunes.types.Duration;
import com.rometools.rome.feed.module.DCModule;
import com.rometools.rome.feed.module.DCModuleImpl;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class RssUtil {

    public String createRssFeed() {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");

            feed.setTitle("");
            feed.setLink("");
            feed.setDescription("");
            feed.setLanguage("");
            feed.setCopyright("");
            feed.setPublishedDate(new Date());
            feed.setGenerator("");

            List<SyndEntry> entries = new ArrayList<>();
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("");
            entry.setLink("");
            entry.setDescription(createDescription(""));
            entry.setPublishedDate(new Date());
            entry.setAuthor("");

            SyndEnclosure enclosure = new SyndEnclosureImpl();
            enclosure.setUrl("");
            enclosure.setLength(0);
            enclosure.setType("");
            List<SyndEnclosure> enclosures = new ArrayList<>();
            enclosures.add(enclosure);
            entry.setEnclosures(enclosures);

            List<Module> entryModules = new ArrayList<>();

            DCModule dcModule = new DCModuleImpl();
            dcModule.setDate(new Date());
            dcModule.setLanguage("");
            dcModule.setRights("");
            entryModules.add(dcModule);

            EntryInformation itunesModule = new EntryInformationImpl();
            itunesModule.setAuthor("");
            itunesModule.setDuration(new Duration(0));
            itunesModule.setImageUri("");
            itunesModule.setExplicit(false);
            itunesModule.setEpisodeType("");
            entryModules.add(itunesModule);

            entry.setModules(entryModules);

            entries.add(entry);
            feed.setEntries(entries);

            List<Module> feedModules = new ArrayList<>();
            FeedInformation feedInfo = new FeedInformationImpl();
            feedInfo.setOwnerName("");
            feedInfo.setOwnerEmailAddress("");
            feedInfo.setCategories(Collections.emptyList());
            feedInfo.setType("");
            feedInfo.setAuthor("");
            feedInfo.setExplicit(false);
            feedInfo.setImageUri("");
            feedModules.add(feedInfo);

            feed.setModules(feedModules);

            SyndFeedOutput output = new SyndFeedOutput();
            return output.outputString(feed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public List<String> getCategories(String filePath) {
        List<String> categories = new ArrayList<>();
        try {
            File file = new File(filePath);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);

            Element rootElement = document.getRootElement();
            Element channel = rootElement.getChild("channel");

            List<Element> categoryElements = channel.getChildren("category", rootElement.getNamespace("itunes"));

            for (Element categoryElement : categoryElements) {
                String category = categoryElement.getAttributeValue("text");
                categories.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void deleteEpisode(String feedUrl, String episodeLink) throws IOException, FeedException {
        // 기존 피드 파일을 읽기
        String filePath = "updated_feed.xml";
        episodeLink = "https://podcasters.spotify.com/pod/show/qnr6ued8ig8/episodes/test-e2j84o5";
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        XmlReader reader = new XmlReader(inputStream);
        SyndFeed feed = new SyndFeedInput().build(reader);

        // 삭제할 항목 찾기
        Iterator<SyndEntry> iterator = feed.getEntries().iterator();
        while (iterator.hasNext()) {
            SyndEntry entry = iterator.next();
            String entryLink = entry.getLink();
            log.info("link={}", episodeLink);
            if (entryLink != null && entryLink.equals(episodeLink)) {
                iterator.remove();
                break;
            }
        }

        // 파일에 변경된 피드를 덮어쓰기로 출력
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

    private SyndContent createDescription(String description) {
        SyndContent content = new SyndContentImpl();
        content.setType("text/plain");
        content.setValue(description);
        return content;
    }
}
