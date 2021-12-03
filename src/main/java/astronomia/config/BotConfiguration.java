package astronomia.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static astronomia.constant.ApplicationConstants.BOT_TOKEN;

@Configuration
public class BotConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(BotConfiguration.class);

    @Bean
    public JDA Jda() {
        JDA jda = null;
        try {
            jda = JDABuilder.createDefault(BOT_TOKEN)
                    .setActivity(Activity.watching("You 😎"))
                    .build().awaitReady();
        } catch (Exception e) {
            LOG.error("Unexpected error have occured starting up the bot... Error: {}", e.getMessage());
        }
        return jda;
    }
}
