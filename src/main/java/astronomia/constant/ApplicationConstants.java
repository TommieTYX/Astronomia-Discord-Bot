package astronomia.constant;

public class ApplicationConstants {
    public static final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");
    public static final String YOUTUBE_API_KEY = System.getenv().get("YOUTUBE_API_KEY");

    public static final String APPLICATION_NAME = "Astronomia";
    public static final String DEFAULT_COMMAND_PREFIX = "~";
    public static final int DEFAULT_MUSIC_PLAYER_VOLUME = 25;

    public static final String BOT_MESSAGE_REQUIRE_VOICE_CHANNEL = "You have yet to call me! Hook me up & " +
            "I can give you more services, you know the command (Join me) 😎";
}
