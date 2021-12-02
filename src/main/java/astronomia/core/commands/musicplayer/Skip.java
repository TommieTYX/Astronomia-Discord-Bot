package astronomia.core.commands.musicplayer;

import astronomia.core.CommandListener;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.apache.maven.shared.utils.StringUtils;
import org.springframework.stereotype.Component;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

@Component
public class Skip extends CommandListener{

    private static String COMMAND_KEYWORD = "skip";
    private static String COMMAND_DESCRIPTION = "Skip / remove music from queue";

    public Skip() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
        addArgs(OptionType.STRING, "id",
                "Music id. By default, skipping current music if music id is not provided", false);
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
                if (StringUtils.isBlank(arg)) {
                    MusicPlayer.getInstance().skipTrack(event.getTextChannel(), false);
                } else {
                    MusicPlayer.getInstance().skipTrackAtIndex(event.getTextChannel(), arg);
                }
            } else {
                event.reply(BOT_MESSAGE_REQUIRE_VOICE_CHANNEL).setEphemeral(true).queue();
            }
        }
    }
}
