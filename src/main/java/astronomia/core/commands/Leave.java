package astronomia.core.commands;

import astronomia.modules.Accessibility;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class Leave extends astronomia.core.commands.AbstractCommand {

    private static String COMMAND_KEYWORD = "leave";
    private static String COMMAND_DESCRIPTION = "Ask bot to leave voice channel";

    public Leave() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        Accessibility.leave(event.getGuild(), event.getInteraction());
    }
}