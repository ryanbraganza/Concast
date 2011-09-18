package baremvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class MyClient {
    private final String username;
    private final String password;
    private final URL url;

    public MyClient(String username, String password, String url)
            throws MalformedURLException {
        this.username = username;
        this.password = password;
        this.url = new URL(url);
    }

    private XmlRpcClient getClient() {
        XmlRpcClient c = new XmlRpcClient();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);

        c.setConfig(config);
        return c;
    }

    private String login(XmlRpcClient c) throws XmlRpcException {
        return (String) c.execute("confluence1.login", new String[] { username,
                password });
    }

    public List<Map<String, String>> getBlogEntries() {
        try {
            XmlRpcClient c = getClient();
            String s = login(c);
            List<Map<String, String>> entries = new ArrayList<Map<String, String>>();
            Object[] stuff = (Object[]) c.execute("confluence1.getBlogEntries",
                    new String[] { s, "intersect" });
            for (Object m_o : stuff) {
                Map m = (Map) m_o;
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", (String) m.get("id"));
                map.put("title", (String) m.get("title"));
                map.put("url", (String) m.get("url"));
                map.put("publishDate", m.get("publishDate").toString());
                entries.add(map);
            }
            return entries;
        } catch (XmlRpcException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getBlog(String blogId) {
        try {
            XmlRpcClient c = getClient();
            String s = login(c);
            Map m = (Map) c.execute("confluence1.getBlogEntry", new String[] {
                    s, blogId });
            Map<String, String> blog = new HashMap<String, String>();
            blog.put("title", (String) m.get("title"));
            blog.put("content", (String) m.get("content"));
            return blog;
        } catch (XmlRpcException e) {
            throw new RuntimeException(e);
        }
    }
}
