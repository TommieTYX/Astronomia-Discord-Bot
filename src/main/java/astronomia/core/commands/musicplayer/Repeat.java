package astronomia.core.commands.musicplayer;

import astronomia.models.UserCommand;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Repeat extends Command {
    public Repeat() {
        super.name = "repeat";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (commandEvent.getTextChannel(), commandEvent.getMember());
        if (isUserConnectedToChannel) {
            MusicPlayer.getInstance().repeatSong(commandEvent.getTextChannel());
        }
    }
}
