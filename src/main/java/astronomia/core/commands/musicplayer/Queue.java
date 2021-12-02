package astronomia.core.commands.musicplayer;

import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

public class Queue extends Command {

    public Queue() {
        super.name = "queue";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (commandEvent.getTextChannel(), commandEvent.getMember());
        if (isUserConnectedToChannel) {
            if (commandEvent.getGuild().getAudioManager().isConnected()) {
                MusicPlayer.getInstance().getTracksList(commandEvent.getTextChannel());
            } else {
                commandEvent.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL);
            }
        }
    }
}
