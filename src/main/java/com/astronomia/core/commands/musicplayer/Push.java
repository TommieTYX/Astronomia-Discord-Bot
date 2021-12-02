package com.astronomia.core.commands.musicplayer;

import com.astronomia.core.CommandListener;
import com.astronomia.modules.musicplayer.MusicPlayer;
import com.astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

import static com.astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

@Component
public class Push extends CommandListener{

    private static String COMMAND_KEYWORD = "push";
    private static String COMMAND_DESCRIPTION = "Push a specific music in queue to play next";

    public Push() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
        addArgs(OptionType.STRING, "id", "Music id", true);
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

                if (CommonUtils.isNotBlankAndCheckNumeric(arg, true)) {
                    MusicPlayer.getInstance().pushSongPosition(event.getTextChannel(),
                            Integer.parseInt(arg), 1);
                } else {
                    event.reply("Give me the correct song Id to push, you dumb dumb! 😎").setEphemeral(true).queue();
                }
            } else {
                event.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL).setEphemeral(true).queue();
            }
        }
    }
}
