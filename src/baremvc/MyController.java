package baremvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {
    
    @Autowired
    private RssMaker rssMaker;
    
    @Autowired
    private ClientFactory clientFactory;
    
    public void setRssMaker(RssMaker rssMaker) {
        this.rssMaker = rssMaker;
    }
    public void setClientFactory(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @RequestMapping("/test")
    public String serve(HttpServletRequest req) throws XmlRpcException, MalformedURLException {
        System.out.println(buildUrl(req, "get/asdf"));
        return "WEB-INF/views/blah.jsp";
    }
    private static String buildUrl(HttpServletRequest req, String path) {
        //return String.format("%s://%s:%s%s/%s", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(), path);
        // TODO FIXME
        return String.format("http://ryan-linux.intersect.org.au/Concast/%s", path);
    }

    @RequestMapping("/podcast.rss")
    public void podcast(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        List<Map<String, String>> summaries = clientFactory.makeClient().getBlogEntries();
        List<BlogSummary> blogIds = new ArrayList<BlogSummary>();
        for (Map<String, String> summary : summaries) {
            String id = summary.get("id");
            String date = summary.get("publishDate");
            String title = summary.get("title");
            String url = summary.get("url");
            blogIds.add(new BlogSummary(id, title, date, url));
        }
        pw.write(rssMaker.makeRss(blogIds));
    }

    @RequestMapping("/get/{blogId}")
    public void get(HttpServletResponse resp,
            @PathVariable("blogId") String blogId) throws IOException {
        resp.setContentType("audio/mp3");
        resp.setHeader("Content-Disposition",
                "attachment;filename=\"blah.mp3\"");
        String content = clientFactory.makeClient().getBlog(blogId).get("content");
        Translit.textToMp3(content, resp.getOutputStream());
    }

}
