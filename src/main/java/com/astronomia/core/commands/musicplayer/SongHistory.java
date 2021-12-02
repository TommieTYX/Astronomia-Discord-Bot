package com.astronomia.core.commands.musicplayer;

import com.astronomia.core.CommandListener;
import com.astronomia.modules.musicplayer.MusicPlayer;
import com.astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

import static com.astronomia.constant.ApplicationConstants.DEFAULT_SONG_HISTORY_SIZE;

@Component
public class SongHistory extends CommandListener{

    private static String COMMAND_KEYWORD = "history";
    private static String COMMAND_DESCRIPTION = "Queue history of the last "+ DEFAULT_SONG_HISTORY_SIZE +" played music";

    public SongHistory() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        boolean isUserConnectedToChannel = CommonUtils.isCurrentUserConnectedToChannel
                (event.getTextChannel(), event.getMember());
        if (isUserConnectedToChannel) {
            MusicPlayer.getInstance().getSongHistory(event.getTextChannel());
        }
    }
}
