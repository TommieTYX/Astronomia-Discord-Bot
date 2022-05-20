package astronomia.core.commands.fun;

import astronomia.core.commands.AbstractCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class Ping extends AbstractCommand {

    private static String COMMAND_KEYWORD = "ping";
    private static String COMMAND_DESCRIPTION = "Calculate ping of the bot";

    public Ping() { init(COMMAND_KEYWORD, COMMAND_DESCRIPTION); }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true)
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                ).queue();
    }
}
