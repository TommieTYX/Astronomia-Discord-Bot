package astronomia.core.commands.musicplayer;

import astronomia.models.UserCommand;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

public class Volume extends Command {

    public Volume() {
        super.name = "volume";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        UserCommand userCommand = MessageHelper.extractUserCommand(commandEvent.getMessage().getContentRaw());

        if (commandEvent.getGuild().getAudioManager().isConnected()) {
            MusicPlayer.getInstance().setVolume(commandEvent.getTextChannel(), userCommand.getMessage());
        } else {
            commandEvent.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL);
        }
    }
}
