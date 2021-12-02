package astronomia.core.commands.fun;

import astronomia.core.CommandListener;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

import static astronomia.constant.ApplicationConstants.BOT_MESSAGE_REQUIRE_VOICE_CHANNEL;

@Component
public class DarylJoker extends CommandListener {

    private static String COMMAND_KEYWORD = "daryljoker";
    private static String COMMAND_DESCRIPTION = "This one I dont need to explain liao la hor? 😎";

    public DarylJoker() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (event.getTextChannel(), event.getMember());
        if (isUserConnectedToChannel) {
            event.reply("<@228148488171552770>, you are a joke!").setTTS(true).queue();
            MusicPlayer.getInstance().loadAndPlay(event.getGuild(), event.getTextChannel(),
                    event.getMember(), "https://www.youtube.com/watch?v=FvRf2ov-glI&ab_channel=AkumaShadow");
        }
    }
}