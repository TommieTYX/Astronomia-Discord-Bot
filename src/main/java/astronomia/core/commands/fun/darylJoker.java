package astronomia.core.commands.fun;

import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class darylJoker extends Command {
    public darylJoker() {
        super.name = "daryljoker";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (commandEvent.getTextChannel(), commandEvent.getMember());
        if (isUserConnectedToChannel) {
            commandEvent.reply("<@228148488171552770>, you are a joke!");
            MusicPlayer.getInstance().loadAndPlay(commandEvent.getGuild(), commandEvent.getTextChannel(),
                    commandEvent.getMember(), "https://www.youtube.com/watch?v=FvRf2ov-glI&ab_channel=AkumaShadow");
        }
    }
}