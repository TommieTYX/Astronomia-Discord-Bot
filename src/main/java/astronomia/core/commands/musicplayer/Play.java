package astronomia.core.commands.musicplayer;

import astronomia.core.CommandListener;
import astronomia.modules.musicplayer.MusicPlayer;
import astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

@Component
public class Play extends CommandListener{

    private static String COMMAND_KEYWORD = "play";
    private static String COMMAND_DESCRIPTION = "Play music";

    public Play() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
        addArgs(OptionType.STRING, "query", "Url / Title. Only support youtube for now", true);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (event.getTextChannel(), event.getMember());
        if (isUserConnectedToChannel) {
            if (!event.getOptions().isEmpty()) {
                MusicPlayer.getInstance().loadAndPlay(event.getGuild(), event.getTextChannel(),
                        event.getMember(), event.getOptions().get(0).getAsString());
            } else {
                event.reply("Please enter a music title / youtube url").setEphemeral(true).queue();
            }
        }
    }
}
