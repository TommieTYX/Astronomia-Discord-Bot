package astronomia.feature;

import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Fun {
    public static void ping(GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        channel.sendMessage("PONG your head ah! ")
                .append(MessageHelper.mention(event.getAuthor())).queue();
    }
}
