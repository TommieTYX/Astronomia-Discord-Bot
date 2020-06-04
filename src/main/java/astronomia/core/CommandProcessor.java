package astronomia.core;

import astronomia.feature.Accessibility;
import astronomia.feature.Fun;
import astronomia.models.Command;
import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static astronomia.constant.ApplicationConstants.COMMAND_PREFIX;

public class CommandProcessor {

    private static final Logger log = LoggerFactory.getLogger(CommandProcessor.class);

    public void run(MessageReceivedEvent event) {
        Command userCommand = MessageHelper.extractCommand(event.getMessage().getContentDisplay());
        switch (userCommand.getCommand().replace(COMMAND_PREFIX, "")) {
            case "ping" :
                Fun.ping(event);
                break;
            default:
                log.info("Command not found!");
        }
    }

    public void guildRun(GuildMessageReceivedEvent event) {
        Command userCommand = MessageHelper.extractCommand(event.getMessage().getContentDisplay());
        switch (userCommand.getCommand().replace(COMMAND_PREFIX, "")) {
            case "join" :
                Accessibility.join(event);
                break;
            case "leave" :
                Accessibility.leave(event);
                break;
            default:
                log.info("Command not found!");
        }
    }
}
