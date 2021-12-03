package astronomia.constant;

public class ApplicationConstants {
    public static final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");
    public static final String YOUTUBE_API_KEY = System.getenv().get("YOUTUBE_API_KEY");

    public static final String APPLICATION_NAME = "Astronomia";
    public static final int DEFAULT_MUSIC_PLAYER_VOLUME = 25;

    public static final int DEFAULT_SONG_HISTORY_SIZE = 20;

    public static final String BOT_MESSAGE_REQUIRE_VOICE_CHANNEL = "You have yet to call me! Hook me up & " +
            "I can give you more services, you know the command (Join me) 😎";

    public static final String BOT_MESSAGE_NO_PERMISSION = "You do not have permission to use this command";
    public static final String BOT_STATUS_CHANGE_SUCCESSFUL = "Bot status have changed successfully";


    //ROLES
    public static final String ROLES_BOT_DEV = "\uD83D\uDCBE Bot Dev";
}
