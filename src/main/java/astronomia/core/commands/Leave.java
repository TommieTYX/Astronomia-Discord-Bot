package astronomia.core.commands;

import astronomia.modules.Accessibility;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Leave extends Command {

    public Leave() {
        super.name = "leave";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        MusicPlayer.getInstance().stopAllTracks(commandEvent.getTextChannel());
        Accessibility.leave(commandEvent.getGuild(), commandEvent.getTextChannel());
    }
}
