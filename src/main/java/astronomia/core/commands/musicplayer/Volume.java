package astronomia.core.commands.musicplayer;

import astronomia.core.commands.AbstractCommand;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.apache.maven.shared.utils.StringUtils;
import org.springframework.stereotype.Component;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;
import static astronomia.constant.ApplicationConstants.DEFAULT_MUSIC_PLAYER_VOLUME;

@Component
public class Volume extends AbstractCommand {

    private static String COMMAND_KEYWORD = "volume";
    private static String COMMAND_DESCRIPTION = "Adjust the volume of bot. Default volume: " + DEFAULT_MUSIC_PLAYER_VOLUME;

    public Volume() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
        addArgs(OptionType.STRING, "volume",
                "Volume", true);
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
                if (event.getOptions().size() > 0 && StringUtils.isNotBlank(arg) && StringUtils.isNumeric(arg)) {
                    MusicPlayer.getInstance().setVolume(event.getTextChannel(), arg, event.getInteraction());
                }
            } else {
                event.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL).setEphemeral(true).queue();;
            }
        }
    }
}
