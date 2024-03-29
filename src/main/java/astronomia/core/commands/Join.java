package astronomia.core.commands;

import astronomia.modules.Accessibility;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class Join extends astronomia.core.commands.AbstractCommand {

    private static String COMMAND_KEYWORD = "join";
    private static String COMMAND_DESCRIPTION = "Invite bot to the voice channel";

    public Join() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        Accessibility.join(event.getGuild(), event.getTextChannel(), event.getMember(), event.getInteraction());
    }
}