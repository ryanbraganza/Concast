package baremvc;

public class Config {
    private String password;
    private String username;
    private String url;

    public Config(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }
    
    public String getPassword() {
        return this.password;
    }
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return this.username;
    }

}
