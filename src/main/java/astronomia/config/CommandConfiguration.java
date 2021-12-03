package astronomia.config;

import astronomia.core.commands.AbstractCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CommandConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(CommandConfiguration.class);

    @Autowired
    List<AbstractCommand> commands;

    @Autowired
    private JDA jda;

    @Bean
    public void initCommands() {
        commands.forEach(cmd -> {
            upsertCommand(cmd)
                    .submit()
                    .thenAcceptAsync(command -> LOG.info("Inserted '" + command.getName() + "' command."))
                    .whenCompleteAsync((value, exception) -> {
                        if (exception != null) {
                            LOG.warn("Could not insert slash command.");
                            exception.printStackTrace();
                        }
                    });
        });
        jda.updateCommands();
    }

    private CommandCreateAction upsertCommand(AbstractCommand cmd) {
        LOG.info("Loading command: /{}", cmd.getCommand());
        jda.addEventListener(cmd);
        if (!cmd.getArgs().isEmpty()) {
            return jda.upsertCommand(cmd.getCommand(), cmd.getDescription()).addOptions(cmd.getArgs());

        }
        return jda.upsertCommand(cmd.getCommand(), cmd.getDescription());
    }
}
