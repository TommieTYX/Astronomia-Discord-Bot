package astronomia.core.commands.musicplayer;

import astronomia.models.UserCommand;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

public class Push extends Command {

    public Push() {
        super.name = "push";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (commandEvent.getTextChannel(), commandEvent.getMember());
        if (isUserConnectedToChannel) {
            UserCommand userCommand = MessageHelper.extractUserCommand(commandEvent.getMessage().getContentRaw());

            if (commandEvent.getGuild().getAudioManager().isConnected()) {
                if (CommonUtils.isNotBlankAndCheckNumeric(userCommand.getMessage(), true)) {
                    MusicPlayer.getInstance().pushSongPosition(commandEvent.getTextChannel(),
                            Integer.parseInt(userCommand.getMessage()), 1);
                } else {
                    commandEvent.reply("Give me the correct song Id to push, you dumb dumb! 😎");
                }
            } else {
                commandEvent.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL);
            }
        }
    }
}
