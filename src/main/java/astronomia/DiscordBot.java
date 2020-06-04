package astronomia;

import astronomia.core.Process;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot extends ListenerAdapter {
    public static void main(String[] args) {
        final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");
        Process process = new Process(BOT_TOKEN);
        process.run();
    }
}
