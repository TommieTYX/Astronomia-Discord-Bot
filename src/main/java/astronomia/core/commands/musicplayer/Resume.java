package astronomia.core.commands.musicplayer;

import astronomia.core.CommandListener;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

@Component
public class Resume extends CommandListener{

    private static String COMMAND_KEYWORD = "resume";
    private static String COMMAND_DESCRIPTION = "Resume paused music";

    public Resume() {
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
                MusicPlayer.getInstance().pauseTrack(event.getTextChannel(), false);
            } else {
                event.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL).setEphemeral(true).queue();
            }
        }
    }
}
