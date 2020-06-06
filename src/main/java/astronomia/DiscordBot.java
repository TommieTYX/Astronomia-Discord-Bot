package astronomia;

import astronomia.core.Process;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static astronomia.constant.ApplicationConstants.BOT_TOKEN;

public class DiscordBot extends ListenerAdapter {
    public static void main(String[] args) {
        Process process = new Process(BOT_TOKEN);
        process.run();
    }
}
