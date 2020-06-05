package astronomia.core;

import astronomia.feature.Accessibility;
import astronomia.feature.Fun;
import astronomia.feature.musicplayer.MusicPlayer;
import astronomia.models.Command;
import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static astronomia.constant.ApplicationConstants.DEFAULT_COMMAND_PREFIX;

public class CommandProcessor {

    private static final Logger log = LoggerFactory.getLogger(CommandProcessor.class);

    public void run(GuildMessageReceivedEvent event) {
        Command userCommand = MessageHelper.extractCommand(event.getMessage().getContentRaw());
        switch (userCommand.getCommand().replace(DEFAULT_COMMAND_PREFIX, "")) {
            case "ping" :
                Fun.ping(event);
                break;
            case "join" :
                Accessibility.join(event.getGuild(), event.getChannel(), event.getMember());
                break;
            case "leave" :
                MusicPlayer.getInstance().stopAllTracks(event.getChannel());
                Accessibility.leave(event.getGuild(), event.getChannel());
                break;
            case "play" :
                MusicPlayer.getInstance().loadAndPlay(event.getGuild(), event.getChannel(), event.getMember(), userCommand.getMessage());
                break;
            case "skip" :
                MusicPlayer.getInstance().skipTrack(event.getChannel());
                break;
            case "pause" :
                MusicPlayer.getInstance().pauseTrack(event.getChannel(),true);
                break;
            case "continue" :
                MusicPlayer.getInstance().pauseTrack(event.getChannel(),false);
                break;
            case "volume" :
                MusicPlayer.getInstance().setVolume(event.getChannel(), userCommand.getMessage());
                break;
            case "queue" :
                MusicPlayer.getInstance().getTracksList(event.getChannel());
                break;
            default:
                log.info("Command not found!");
                event.getChannel().sendMessage("What are you talking about, you talking to me? Get your command right dumb dumb! 😎").queue();
        }
    }
}
