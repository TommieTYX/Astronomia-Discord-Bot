package astronomia.core.commands.musicplayer;

import astronomia.models.UserCommand;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.maven.shared.utils.StringUtils;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

public class Skip extends Command {

    public Skip() {
        super.name = "skip";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (commandEvent.getTextChannel(), commandEvent.getMember());
        if (isUserConnectedToChannel) {
            UserCommand userCommand = MessageHelper.extractUserCommand(commandEvent.getMessage().getContentRaw());

            if (commandEvent.getGuild().getAudioManager().isConnected()) {
                if (StringUtils.isBlank(userCommand.getMessage())) {
                    MusicPlayer.getInstance().skipTrack(commandEvent.getTextChannel());
                } else {
                    MusicPlayer.getInstance().skipTrackAtIndex(commandEvent.getTextChannel(), userCommand.getMessage());
                }
            } else {
                commandEvent.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL);
            }
        }
    }
}
