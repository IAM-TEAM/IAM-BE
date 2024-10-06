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
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
                                String newDescription, List<String> mainCategories, List<String> subCategories,
                                String email, String imageUrl) {
        try {
            Date now = convertToUtcDate(LocalDateTime.now());
            // 기존 피드 읽기
            URL feedUrl = new URL(existingFeedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            // 새로운 정보로 업데이트
            feed.setTitle(newTitle);
            feed.setLink(LinkUtil.LINK.getLink());
            feed.setDescription(newDescription);

            // 추가: SyndImage 설정
            SyndImage image = new SyndImageImpl();
            image.setTitle(newTitle);
            image.setUrl(imageUrl);
            image.setLink(newLink);
            image.setWidth(1400);
            image.setHeight(1400);
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

            // iTunes 카테고리 생성
            String itunesCategoriesXml = createItunesCategories(mainCategories, subCategories);

            // 피드 출력
            SyndFeedOutput output = new SyndFeedOutput();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            output.output(feed, writer);
            writer.flush();

            String feedString = byteArrayOutputStream.toString(StandardCharsets.UTF_8);

            // 기존 카테고리 제거 후 새 카테고리 삽입
            String modifiedFeedString = deleteItunesCategories(feedString, itunesCategoriesXml);
            modifiedFeedString = insertItunesCategories(modifiedFeedString, itunesCategoriesXml);

            //log.info("Modified RSS Feed:\n{}", modifiedFeedString);

            return modifiedFeedString;
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
            feed.setGenerator("IAM");

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

            SyndFeedOutput output = new SyndFeedOutput();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            output.output(feed, writer);
            writer.flush();

            //            result = addAtomNamespaceAndFormat(result, "https://anchor.fm/s/f5858a40/podcast/rss");
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
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
    }

    public Map<String, List<String>> getCategories(String rssFeedUrl) {
        Map<String, List<String>> categoryDetailMap = new HashMap<>();
        try {
            URL url = new URL(rssFeedUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream()) {
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(inputStream);

                Element rootElement = document.getRootElement();
                Element channel = rootElement.getChild("channel");

                // itunes namespace를 사용해서 category 요소 찾기
                Namespace itunesNamespace = Namespace.getNamespace("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd");
                List<Element> categoryElements = channel.getChildren("category", itunesNamespace);

                for (Element categoryElement : categoryElements) {
                    String mainCategory = categoryElement.getAttributeValue("text");
                    List<String> detailCategories = new ArrayList<>();

                    // 하위 카테고리(detailCategory)를 찾음
                    List<Element> subCategoryElements = categoryElement.getChildren("category", itunesNamespace);
                    for (Element subCategoryElement : subCategoryElements) {
                        String detailCategory = subCategoryElement.getAttributeValue("text");
                        detailCategories.add(detailCategory);
                    }

                    // mainCategory와 detailCategories 매핑
                    categoryDetailMap.put(mainCategory, detailCategories);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryDetailMap;
    }

    public void deleteEpisode(String feedUrl, String episodeLink) throws IOException, FeedException {
        // 기존 피드 파일을 읽기
        URL url = new URL(feedUrl);
        XmlReader reader = new XmlReader(url);
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

//        Element guid = new Element("guid");
//        guid.setText(link);
//        guid.setAttribute("isPermaLink", "false");
//        entry.getForeignMarkup().add(guid);

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

    private String createItunesCategories(List<String> mainCategories, List<String> subCategories) {
        StringBuilder xmlBuilder = new StringBuilder();

        for (int i = 0; i < mainCategories.size(); i++) {
            String parentCategory = mainCategories.get(i).replace("&", "&amp;");  // 부모 카테고리
            String childCategory = (i < subCategories.size()) ? subCategories.get(i).replace("&", "&amp;") : null;  // 자식 카테고리

            // 부모 카테고리 열기
            xmlBuilder.append("<itunes:category text=\"").append(parentCategory).append("\">");

            // 자식 카테고리 확인 후 추가
            if (childCategory != null && !childCategory.isEmpty()) {
                xmlBuilder.append("<itunes:category text=\"").append(childCategory).append("\" />");
            }

            // 부모 카테고리 닫기
            xmlBuilder.append("</itunes:category>");
        }

        return xmlBuilder.toString();
    }

    private String insertItunesCategories(String feedString, String itunesCategoriesXml) {
        // <itunes:type> 태그 바로 뒤에 카테고리를 삽입
        String insertionPoint = "<itunes:type>";
        int insertPosition = feedString.indexOf(insertionPoint);

        if (insertPosition > -1) {
            // <itunes:type> 태그 뒤에 카테고리 XML을 삽입
            int endOfTypeTag = feedString.indexOf("</itunes:type>", insertPosition) + "</itunes:type>".length();
            return feedString.substring(0, endOfTypeTag)
                    + itunesCategoriesXml
                    + feedString.substring(endOfTypeTag);
        }

        return feedString;  // <itunes:type> 태그를 못 찾으면 원본 반환
    }

    private String deleteItunesCategories(String feedString, String itunesCategoriesXml) {
        try {
            // XML 문자열을 Document로 변환
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // 네임스페이스 지원
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(feedString.getBytes());
            org.w3c.dom.Document doc = builder.parse(inputStream);

            // 모든 itunes:category 태그를 찾아서 삭제
            NodeList categoryNodes = doc.getElementsByTagNameNS("*", "category"); // 네임스페이스 무시하고 검색
            for (int i = categoryNodes.getLength() - 1; i >= 0; i--) {
                Node node = categoryNodes.item(i);
                node.getParentNode().removeChild(node);
            }

            // 새로운 카테고리 삽입을 위한 DocumentFragment 생성
            DocumentFragment fragment = doc.createDocumentFragment();

            // 새로운 카테고리 XML을 DocumentFragment에 추가
            fragment.appendChild(doc.createTextNode(itunesCategoriesXml));

            // <itunes:owner> 태그를 찾아 그 뒤에 카테고리 삽입
            NodeList ownerNodes = doc.getElementsByTagNameNS("*", "owner");
            if (ownerNodes.getLength() > 0) {
                Node ownerNode = ownerNodes.item(0);
                ownerNode.getParentNode().insertBefore(fragment, ownerNode.getNextSibling());
            }

            // Document를 다시 문자열로 변환
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // 추가: 공백 제거를 위한 설정
            transformer.setOutputProperty(OutputKeys.INDENT, "no"); // 공백 없이 출력
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // XML 선언 유지
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0"); // 들여쓰기 없앰
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return feedString;
        }
    }

    public String addAtomNamespaceAndFormat(String feedString, String link) {
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
            selfLink.setAttribute("href", link);
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
