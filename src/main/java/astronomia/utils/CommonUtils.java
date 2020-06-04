package astronomia.utils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommonUtils {

    public static String getSenderNameFromEvent(MessageReceivedEvent event) {
        String name;
        if (event.getMessage().isWebhookMessage()) {
            name = event.getAuthor().getName();                //If this is a Webhook message, then there is no Member associated
        } else {
            name = event.getMember().getEffectiveName();       //This will either use the Member's nickname if they have one,
        }
        return name;
    }
}
