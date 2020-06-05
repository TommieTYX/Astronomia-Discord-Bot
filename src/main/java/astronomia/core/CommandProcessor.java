package astronomia.core;

import astronomia.feature.Accessibility;
import astronomia.feature.Fun;
import astronomia.feature.musicplayer.MusicPlayer;
import astronomia.models.Command;
import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static astronomia.constant.ApplicationConstants.COMMAND_PREFIX;

public class CommandProcessor {

    private static final Logger log = LoggerFactory.getLogger(CommandProcessor.class);

    public void run(GuildMessageReceivedEvent event) {
        Command userCommand = MessageHelper.extractCommand(event.getMessage().getContentRaw());
        switch (userCommand.getCommand().replace(COMMAND_PREFIX, "")) {
            case "ping" :
                Fun.ping(event);
                break;
            case "join" :
                Accessibility.join(event.getGuild(), event.getChannel(), event.getMember());
                break;
            case "leave" :
                MusicPlayer.stopAllTracks(event.getChannel());
                Accessibility.leave(event.getGuild(), event.getChannel());
                break;
            case "play" :
                MusicPlayer.loadAndPlay(event.getGuild(), event.getChannel(), event.getMember(), userCommand.getMessage());
                break;
            case "skip" :
                MusicPlayer.skipTrack(event.getChannel());
                break;
            case "pause" :
                MusicPlayer.pauseTrack(event.getChannel(),true);
                break;
            case "continue" :
                MusicPlayer.pauseTrack(event.getChannel(),false);
                break;
            default:
                log.info("Command not found!");
                event.getChannel().sendMessage("What are you talking about, you talking to me? Get your command right dumb dumb! 😎").queue();
        }
    }
}
