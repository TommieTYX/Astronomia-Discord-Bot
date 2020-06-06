package astronomia;

import astronomia.core.CommandListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

import static astronomia.constant.ApplicationConstants.BOT_TOKEN;

public class DiscordBot extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Process.class);

    public static void main(String[] args) {
        JDABuilder builder = new JDABuilder(BOT_TOKEN);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.ACTIVITY);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Disable compression (not recommended)
        builder.setCompression(Compression.NONE);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("You 😎"));
        // Add Command Listener
        builder.addEventListeners(new CommandListener());

        try {
            builder.build();
        } catch (
                LoginException e) {
            log.error("Error logging in bot account. Error: {}", e.getMessage());
        }
    }
}
