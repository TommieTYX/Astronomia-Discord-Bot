package astronomia.core;

import astronomia.feature.Accessibility;
import astronomia.feature.Fun;
import astronomia.feature.musicplayer.MusicPlayer;
import astronomia.models.Command;
import astronomia.utils.CommonUtils;
import astronomia.utils.MessageHelper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.maven.shared.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static astronomia.constant.ApplicationConstants.DEFAULT_COMMAND_PREFIX;

public class CommandProcessor {

    private static final Logger log = LoggerFactory.getLogger(CommandProcessor.class);

    public void run(GuildMessageReceivedEvent event) {
        Command userCommand = MessageHelper.extractCommand(event.getMessage().getContentRaw());
        if(userCommand.getCommand().replace(DEFAULT_COMMAND_PREFIX, "").equals("join")){
            Accessibility.join(event.getGuild(), event.getChannel(), event.getMember());
        }else if(userCommand.getCommand().replace(DEFAULT_COMMAND_PREFIX, "").equals("play")){
            MusicPlayer.getInstance().loadAndPlay(event.getGuild(), event.getChannel(), event.getMember(), userCommand.getMessage());
        }else if (event.getGuild().getAudioManager().isConnected()){
            switch (userCommand.getCommand().replace(DEFAULT_COMMAND_PREFIX, "")) {
                case "ping":
                    Fun.ping(event);
                    break;
                case "leave":
                    MusicPlayer.getInstance().stopAllTracks(event.getChannel());
                    Accessibility.leave(event.getGuild(), event.getChannel());
                    break;
                case "skip":
                    if(StringUtils.isBlank(userCommand.getMessage())) {
                        MusicPlayer.getInstance().skipTrack(event.getChannel());
                    }else {
                        MusicPlayer.getInstance().skipTrackAtIndex(event.getChannel(), userCommand.getMessage());
                    }
                    break;
                case "pause":
                    MusicPlayer.getInstance().pauseTrack(event.getChannel(), true);
                    break;
                case "continue":
                    MusicPlayer.getInstance().pauseTrack(event.getChannel(), false);
                    break;
                case "volume":
                    MusicPlayer.getInstance().setVolume(event.getChannel(), userCommand.getMessage());
                    break;
                case "queue":
                    MusicPlayer.getInstance().getTracksList(event.getChannel());
                    break;
                case "push":
                    if(CommonUtils.isNotBlankAndCheckNumeric(userCommand.getMessage(), true)) {
                        MusicPlayer.getInstance().pushSongPosition(event.getChannel(), Integer.parseInt(userCommand.getMessage()), 1);
                    }else{
                        event.getChannel().sendMessage("Give me the correct song Id to push, you dumb dumb! 😎").queue();
                    }
                    break;
                default:
                    log.info("Command not found!");
                    event.getChannel().sendMessage("You talking to me? Get your command right dumb dumb! 😎").queue();
            }
        }else{
            event.getChannel().sendMessage("You have yet to call me! Hook me up & I can give you more services, you know the command (Join me) 😎").queue();
        }
    }
}
