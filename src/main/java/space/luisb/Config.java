package space.luisb;

public class Config {
    private int port = 1288;
    private String username;
    private final int MAX_TTL = 5;
    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static int getPort() {
        return getInstance().port;
    }

    public static String getUsername() {
        return getInstance().username;
    }

    public static int getMaxTTL() {
        return getInstance().MAX_TTL;
    }

    public static void setPort(int port) {
        getInstance().port = port;
    }

    public static void setUsername(String username) {
        getInstance().username = username;
    }
}
