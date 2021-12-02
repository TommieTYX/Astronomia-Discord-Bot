package com.astronomia.core.commands;

import com.astronomia.core.CommandListener;
import com.astronomia.modules.Accessibility;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class Leave extends CommandListener{

    private static String COMMAND_KEYWORD = "leave";
    private static String COMMAND_DESCRIPTION = "Ask bot to leave voice channel";

    public Leave() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        Accessibility.leave(event.getGuild(), event.getTextChannel());
    }
}