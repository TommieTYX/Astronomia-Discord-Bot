package astronomia.constant;

public class ApplicationConstants {
    public static final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");
    public static final String YOUTUBE_API_KEY = System.getenv().get("YOUTUBE_API_KEY");

    public static final String APPLICATION_NAME = "Astronomia";
    public static final String DEFAULT_COMMAND_PREFIX = "~";
    public static final int DEFAULT_MUSIC_PLAYER_VOLUME = 25;
}
