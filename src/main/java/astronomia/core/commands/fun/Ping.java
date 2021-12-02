package astronomia.core.commands.fun;

import astronomia.core.CommandListener;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class Ping extends CommandListener{

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



//    @Override
//    public void onMessageReceived(MessageReceivedEvent event)
//    {
//        if (event.isFromType(ChannelType.PRIVATE))
//        {
//            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
//                    event.getMessage().getContentDisplay());
//        }
//        else
//        {
//            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
//                    event.getTextChannel().getName(), event.getMember().getEffectiveName(),
//                    event.getMessage().getContentDisplay());
//        }
//    }
}
