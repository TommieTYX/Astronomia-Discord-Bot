package astronomia.core;

import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static astronomia.constant.ApplicationConstants.DEFAULT_COMMAND_PREFIX;

public class CommandListener extends ListenerAdapter {

    private CommandProcessor commandProcessor = new CommandProcessor();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String msg = message.getContentDisplay();

        if (!event.getAuthor().isBot()) {
            if (isCommand(msg)) {
                commandProcessor.run(event);
            }
        }

        super.onGuildMessageReceived(event);
    }

    private boolean isCommand(String msg) {
        return !msg.isBlank() && MessageHelper.extractCommand(msg).getCommand().startsWith(DEFAULT_COMMAND_PREFIX);
    }
}
