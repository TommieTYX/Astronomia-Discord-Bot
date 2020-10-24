package astronomia.core.commands.musicplayer;

import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SongHistory extends Command {
    public SongHistory() {
        super.name = "history";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (commandEvent.getTextChannel(), commandEvent.getMember());
        if (isUserConnectedToChannel) {
            MusicPlayer.getInstance().getSongHistory(commandEvent.getTextChannel());
        }
    }
}
