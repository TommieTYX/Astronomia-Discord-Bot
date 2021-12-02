package astronomia.config;

import astronomia.core.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static astronomia.constant.ApplicationConstants.BOT_TOKEN;

@Configuration
public class BotConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(BotConfiguration.class);

    @Autowired
    List<CommandListener> commands;

    @Bean
    public JDA getJda() {
        JDA jda = null;
        try {
            jda = JDABuilder.createDefault(BOT_TOKEN)
                    .setActivity(Activity.playing("Type /ping"))
                    .build()
                    .awaitReady();
        } catch (Exception e) {
            LOG.error("Unexpected error have occured starting up the bot... Error: {}", e.getMessage());
        }
        return jda;
    }

    @Bean
    public void initCommands() {
        commands.forEach(cmd -> {
            LOG.info("Loading command: /{}", cmd.getCommand());
            getJda().addEventListener(cmd);

            if (cmd.getArgs().isEmpty()) {
                getJda().upsertCommand(cmd.getCommand(), cmd.getDescription()).queue();
            } else {
                getJda().upsertCommand(cmd.getCommand(), cmd.getDescription())
                        .addOptions(cmd.getArgs())
                        .queue();
            }
        });
    }
}
