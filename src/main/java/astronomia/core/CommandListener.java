package astronomia.core;

import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static astronomia.constant.ApplicationConstants.COMMAND_PREFIX;

public class CommandListener extends ListenerAdapter {

    private CommandProcessor commandProcessor = new CommandProcessor();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String msg = message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {
            if (isCommand(msg)) {
                commandProcessor.run(event);
            }
        }
    }
    // Only listening to guild messages.
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // Good practise to ignore bots.
        if(event.getAuthor().isBot()) {
            return;
        }
       commandProcessor.guildRun(event);
    }


    private boolean isCommand(String msg) {
        return !msg.isBlank() && MessageHelper.extractCommand(msg).getCommand().startsWith(COMMAND_PREFIX);
    }
}
