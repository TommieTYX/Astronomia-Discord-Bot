package com.astronomia.core.commands.musicplayer;

import com.astronomia.core.CommandListener;
import com.astronomia.modules.musicplayer.MusicPlayer;
import com.astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

import static com.astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

@Component
public class Pause extends CommandListener {

    private static String COMMAND_KEYWORD = "pause";
    private static String COMMAND_DESCRIPTION = "Pause currently playing music";

    public Pause() {
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
                MusicPlayer.getInstance().pauseTrack(event.getTextChannel(), true);
            } else {
                event.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL).setEphemeral(true).queue();
            }
        }
    }
}