package baremvc;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RssMaker {

    public String makeRss(List<BlogSummary> summaries) {
        StringBuilder sb = new StringBuilder();
        for (BlogSummary summary : summaries) {
            String entry = makeEntry(summary.id, summary.title, summary.date, summary.url);
            sb.append(entry);
        }
        return String.format(template, sb.toString());
    }

    private String makeEntry(String id, String title, String date, String link) {
        String url = String.format("http://ryan-linux.intersect.org.au/Concast/get/%s", id); // TODO
        String guid = link;
        return String.format(entryTemplate, title, link, guid, url, date);
    }

    private static final String template = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<rss version=\"2.0\">" + "<channel>" + "<title>concast</title>"
            + "<description>concast podcast</description>"
            + "<link>http://ryan-linux.intersect.org.au/Concast/podcast.rss</link>" // FIXME 
            + "%s"
            + "</channel></rss>";

    private static final String entryTemplate = "\n<item>"
            + "\n <title>%s</title>" + "<link>%s</link>                       "
            + "\n <guid>%s</guid>                "
            + "\n <description> no description</description>"
            + "\n <enclosure url=\"%s\" length=\"0\" type=\"audio/mp3\"/>"
            + "\n <category>Podcasts</category>" + "<pubDate>%s</pubDate>"
            + "\n</item>";

}
