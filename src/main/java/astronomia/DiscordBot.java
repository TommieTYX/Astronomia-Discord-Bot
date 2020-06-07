package astronomia;

import astronomia.core.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import static astronomia.constant.ApplicationConstants.BOT_TOKEN;

public class DiscordBot extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {

        JDA jda = JDABuilder.createDefault(BOT_TOKEN).build();
        jda.addEventListener(new CommandListener().build());
    }
}
