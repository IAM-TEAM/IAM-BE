package kr.iam.global.util;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.modules.itunes.FeedInformation;
import com.rometools.modules.itunes.FeedInformationImpl;
import com.rometools.modules.itunes.types.Category;
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
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
public class RssUtil {

    public String updateRssFeed(String existingFeedUrl, String newTitle, String newLink, String newAuthor,
                                String newDescription, String category, String email, String imageUrl) {
        try {
            Date now = convertToUtcDate(LocalDateTime.now());
            // 기존 피드 읽기
            URL feedUrl = new URL(existingFeedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            // 새로운 정보로 업데이트
            feed.setTitle(newTitle);
            feed.setLink(newLink);
            feed.setDescription(newDescription);

            // 추가: SyndImage 설정
            SyndImage image = new SyndImageImpl();
            image.setTitle(newTitle);
            image.setUrl(imageUrl);
            image.setLink(newLink);
            feed.setImage(image);

            List<Module> modules = feed.getModules();
            DCModule dcModule = null;
            FeedInformation itunesInfo = null;
            for (Module module : modules) {
                if (module instanceof DCModule) {
                    dcModule = (DCModule) module;
                    dcModule.setRights(newAuthor);
                    dcModule.setDate(now);
                } else if (module instanceof FeedInformation) {
                    itunesInfo = (FeedInformation) module;
                }
            }
            if (dcModule == null) {
                dcModule = new DCModuleImpl();
                dcModule.setRights(newAuthor);
                dcModule.setDate(now);
                modules.add(dcModule);
            }

            if (itunesInfo == null) {
                itunesInfo = new FeedInformationImpl();
                modules.add(itunesInfo);
            }

            // iTunes 정보 업데이트
            itunesInfo.setOwnerEmailAddress(email);
            itunesInfo.setOwnerName(newAuthor);
            itunesInfo.setAuthor(newAuthor);
            itunesInfo.setImageUri(imageUrl);
            itunesInfo.setSummary(newDescription);
            itunesInfo.setExplicit(false);
            itunesInfo.setType("episodic");

            List<Category> itunesCategories = new ArrayList<>();
            itunesCategories.add(new Category(category));
            itunesInfo.setCategories(itunesCategories);

            feed.setModules(modules);

            SyndFeedOutput output = new SyndFeedOutput();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            output.output(feed, writer);
            writer.flush();

            String feedString = byteArrayOutputStream.toString(StandardCharsets.UTF_8);

            // XML 수정 및 포맷팅
            //feedString = addAtomNamespaceAndFormat(feedString);

            log.info("Modified RSS Feed:\n{}", feedString);

            return feedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 최초 로그인 시 Rss Feed가 없다면 생성
     * 필요한 것
     * 채널 타이틀, 생성 유저 페이지 링크, 유저 이름, 채널 묘사
     * @return
     */
    public String createRssFeed() {
        LocalDateTime date = LocalDateTime.now();
        Date now = convertToUtcDate(date);
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");

            feed.setTitle("테스팅");
            feed.setLink("");
            feed.setDescription("");
            feed.setLanguage("ko");
            feed.setCopyright("");
            feed.setPublishedDate(now);
            feed.setGenerator("");

            // 추가: SyndImage 설정
            SyndImage image = new SyndImageImpl();
            image.setTitle("");
            image.setUrl("");
            image.setLink("");
            feed.setImage(image);

            List<Module> feedModules = new ArrayList<>();

            DCModule dcModule = new DCModuleImpl();
            dcModule.setDate(now);
            dcModule.setLanguage("ko");
            dcModule.setRights("");
            feedModules.add(dcModule);

            FeedInformation feedInfo = new FeedInformationImpl();
            feedInfo.setOwnerName("");
            feedInfo.setOwnerEmailAddress("");
            feedInfo.setCategories(Collections.emptyList());
            feedInfo.setType("");
            feedInfo.setAuthor("");
            feedInfo.setExplicit(false);
            feedInfo.setImageUri("");
            feedInfo.setSummary("test2");
            feedModules.add(feedInfo);

            feed.setModules(feedModules);

            // atom:link 태그 설정
            List<SyndLink> list = new ArrayList<>();
            SyndLinkImpl selfLink = new SyndLinkImpl();
            selfLink.setRel("self");
            selfLink.setHref("https://anchor.fm/s/f5858a40/podcast/rss");
            selfLink.setType("application/rss+xml");
            list.add(selfLink);

            SyndLinkImpl hubLink = new SyndLinkImpl();
            hubLink.setRel("hub");
            hubLink.setHref("https://pubsubhubbub.appspot.com/");
            list.add(hubLink);
            feed.setLinks(list);

            SyndFeedOutput output = new SyndFeedOutput();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            output.output(feed, writer);
            writer.flush();

            String result =  byteArrayOutputStream.toString(StandardCharsets.UTF_8);
            result = addAtomNamespaceAndFormat(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addEpisode(String feedUrl, SyndEntry newEpisode) throws IOException, FeedException {
        // 기존 피드 파일을 읽기
        URL url = new URL(feedUrl);
        XmlReader reader = new XmlReader(url);
        SyndFeed feed = new SyndFeedInput().build(reader);

        // 기존 피드에 새로운 항목 추가
        feed.getEntries().add(0, newEpisode);  // Add at the beginning of the list

        // 새로운 피드를 String으로 변환
        StringWriter stringWriter = new StringWriter();
        SyndFeedOutput output = new SyndFeedOutput();
        output.output(feed, stringWriter);
        String updatedFeed = stringWriter.toString();

        // URL에 새로운 피드를 덮어쓰기
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/rss+xml; charset=UTF-8");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(updatedFeed.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to update feed: HTTP response code " + responseCode);
        }

        // 리소스 정리
        reader.close();
        stringWriter.close();
//        String filePath = "updated_feed.xml";
////        URL url = new URL(feedUrl);
////        XmlReader reader = new XmlReader(url);
//        File file = new File(filePath);
//        FileInputStream inputStream = new FileInputStream(file);
//        XmlReader reader = new XmlReader(inputStream);
//        SyndFeed feed = new SyndFeedInput().build(reader);
//
//        // 기존 피드에 새로운 항목 추가
//        feed.getEntries().add(0, newEpisode);  // Add at the beginning of the list
//
//        // 파일에 새로운 피드를 덮어쓰기로 출력
//        FileWriter writer = new FileWriter(file);  // 기존 파일 경로를 사용하여 파일을 덮어쓰기
//        SyndFeedOutput output = new SyndFeedOutput();
//        output.output(feed, writer);
//        writer.close();  // 리소스 정리
//        reader.close();  // XML Reader 닫기
    }

    public List<String> getCategories(String rssFeedUrl) {
        List<String> categories = new ArrayList<>();
        try {
            URL url = new URL(rssFeedUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream()) {
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(inputStream);

                Element rootElement = document.getRootElement();
                Element channel = rootElement.getChild("channel");

                List<Element> categoryElements = channel.getChildren("category", rootElement.getNamespace("itunes"));

                for (Element categoryElement : categoryElements) {
                    String category = categoryElement.getAttributeValue("text");
                    categories.add(category);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
//    public List<String> getCategories(String filePath) {
//        List<String> categories = new ArrayList<>();
//        try {
//            File file = new File(filePath);
//            SAXBuilder saxBuilder = new SAXBuilder();
//            Document document = saxBuilder.build(file);
//
//            Element rootElement = document.getRootElement();
//            Element channel = rootElement.getChild("channel");
//
//            List<Element> categoryElements = channel.getChildren("category", rootElement.getNamespace("itunes"));
//
//            for (Element categoryElement : categoryElements) {
//                String category = categoryElement.getAttributeValue("text");
//                categories.add(category);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return categories;
//    }

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
        Date date = convertToUtcDate(pubDate);
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

        Element pubDateElement = new Element("pubDate");
        pubDateElement.setText(formatPubDate(date));
        entry.getForeignMarkup().add(pubDateElement);

        Element dcDateElement = new Element("date", dcNamespace);
        dcDateElement.setText(formatDcDate(date));
        entry.getForeignMarkup().add(dcDateElement);
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

    public Date convertToUtcDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        return Date.from(zonedDateTime.toInstant());
    }

    public String formatPubDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public String formatDcDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

//    private Date convertToKstDate(LocalDateTime localDateTime) {
//        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul")).minusHours(3);
//        return Date.from(zonedDateTime.toInstant());
//    }
//
//    public Date convertToKstDateSame(LocalDateTime localDateTime) {
//        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
//        return Date.from(zonedDateTime.toInstant());
//    }
//
//    public String formatPubDate(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//        return sdf.format(date);
//    }
//
//    public String formatDcDate(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//        return sdf.format(date);
//    }

    private String addAtomNamespaceAndFormat(String feedString) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(feedString));

            Element rootElement = document.getRootElement();
            Namespace atomNamespace = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");

            // Add atom namespace
            rootElement.addNamespaceDeclaration(atomNamespace);

            // Add atom:link elements
            Element selfLink = new Element("link", atomNamespace);
            selfLink.setAttribute("rel", "self");
            selfLink.setAttribute("href", "https://anchor.fm/s/f5858a40/podcast/rss");
            selfLink.setAttribute("type", "application/rss+xml");

            Element hubLink = new Element("link", atomNamespace);
            hubLink.setAttribute("rel", "hub");
            hubLink.setAttribute("href", "https://pubsubhubbub.appspot.com/");

            rootElement.getChild("channel").addContent(selfLink);
            rootElement.getChild("channel").addContent(hubLink);

            // Format XML
            Format format = Format.getPrettyFormat();
            format.setIndent("    ");  // Set indentation
            format.setLineSeparator("\n");  // Set line separator

            XMLOutputter xmlOutputter = new XMLOutputter(format);
            StringWriter stringWriter = new StringWriter();
            xmlOutputter.output(document, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return feedString;
        }
    }
}
