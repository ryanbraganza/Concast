package baremvc;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientFactory {

    @Autowired
    private Config config;

    public void setConfig(Config config) {
        this.config = config;
    }

    public MyClient makeClient() {
        try {
            MyClient c = new MyClient(config.getUsername(),
                    config.getPassword(), config.getUrl());
            return c;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
