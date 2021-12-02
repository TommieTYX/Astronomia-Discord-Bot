package astronomia.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.maven.shared.utils.StringUtils;

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

    public static boolean isNotBlankAndCheckNumeric(String currentStr, boolean checkNumeric){
        if(!StringUtils.isBlank(currentStr)) {
            if (checkNumeric){
                if(StringUtils.isNumeric(currentStr)) {
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isCurrentUserConnectedToChannel(TextChannel channel, Member member) {
        VoiceChannel connectedChannel = member.getVoiceState().getChannel();
        // Checks if they are in a channel
        if (connectedChannel == null) {
            channel.sendMessage("You are not connected to a voice channel!").queue();
            return false;
        }
        return true;
    }
}
