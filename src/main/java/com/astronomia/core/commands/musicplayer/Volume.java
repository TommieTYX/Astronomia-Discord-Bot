//package astronomia.core.commands.musicplayer;
//
//import astronomia.models.UserCommand;
//import astronomia.modules.musicplayer.MusicPlayer;
//import astronomia.utils.CommonUtils;
//import astronomia.utils.MessageHelper;
//import com.jagrosh.jdautilities.command.Command;
//import com.jagrosh.jdautilities.command.CommandEvent;
//
//import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;
//
//public class Volume extends Command {
//
//    public Volume() {
//        super.name = "volume";
//    }
//
//    @Override
//    protected void execute(CommandEvent commandEvent) {
//        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
//                (commandEvent.getTextChannel(), commandEvent.getMember());
//        if (isUserConnectedToChannel) {
//            UserCommand userCommand = MessageHelper.extractUserCommand(commandEvent.getMessage().getContentRaw());
//
//            if (commandEvent.getGuild().getAudioManager().isConnected()) {
//                MusicPlayer.getInstance().setVolume(commandEvent.getTextChannel(), userCommand.getMessage());
//            } else {
//                commandEvent.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL);
//            }
//        }
//    }
//}

package com.astronomia.core.commands.musicplayer;

import com.astronomia.core.CommandListener;
import com.astronomia.modules.musicplayer.MusicPlayer;
import com.astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

import static com.astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;
import static com.astronomia.constant.ApplicationConstants.DEFAULT_MUSIC_PLAYER_VOLUME;

@Component
public class Volume extends CommandListener{

    private static String COMMAND_KEYWORD = "volume";
    private static String COMMAND_DESCRIPTION = "Adjust the volume of bot. Default volume: " + DEFAULT_MUSIC_PLAYER_VOLUME;

    public Volume() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (event.getTextChannel(), event.getMember());
        if (isUserConnectedToChannel) {
            if (event.getGuild().getAudioManager().isConnected()) {
                String arg = event.getOptions().get(0).getAsString();
                MusicPlayer.getInstance().setVolume(event.getTextChannel(), arg);
            } else {
                event.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL).setEphemeral(true).queue();;
            }
        }
    }
}
