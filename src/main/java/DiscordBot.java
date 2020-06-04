import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    public static void main(String[] args) {
        final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");

        JDABuilder builder = new JDABuilder(BOT_TOKEN);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Disable compression (not recommended)
        builder.setCompression(Compression.NONE);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("You... 😎"));

        try {
            builder.build();
        } catch (LoginException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
