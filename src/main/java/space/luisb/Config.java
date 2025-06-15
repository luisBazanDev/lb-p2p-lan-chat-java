package space.luisb;

public class Config {
    private String host;
    private int port;
    private String username;
    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static String getHost() {
        return getInstance().host;
    }

    public static int getPort() {
        return getInstance().port;
    }

    public static String getUsername() {
        return getInstance().username;
    }

    public static void setHost(String host) {
        getInstance().host = host;
    }

    public static void setPort(int port) {
        getInstance().port = port;
    }

    public static void setUsername(String username) {
        getInstance().username = username;
    }
}
