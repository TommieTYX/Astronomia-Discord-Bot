package astronomia.core.commands.musicplayer;

import astronomia.models.UserCommand;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Play extends Command {

    public Play() {
        super.name = "play";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        UserCommand userCommand = MessageHelper.extractUserCommand(commandEvent.getMessage().getContentRaw());
        MusicPlayer.getInstance().loadAndPlay(commandEvent.getGuild(), commandEvent.getTextChannel(),
                commandEvent.getMember(), userCommand.getMessage());
    }
}
