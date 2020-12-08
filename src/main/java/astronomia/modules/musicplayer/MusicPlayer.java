package astronomia.modules.musicplayer;

import astronomia.modules.Accessibility;
import astronomia.utils.MessageHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.maven.shared.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MusicPlayer {
    private static final Logger log = LoggerFactory.getLogger(MusicPlayer.class);

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final YoutubeHandler youtube;

    private static MusicPlayer musicPlayer = null;

    private MusicPlayer() {
        musicManagers = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();
        youtube = new YoutubeHandler();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            synchronized (MusicPlayer.class) {
                if (musicPlayer == null) {
                    musicPlayer = new MusicPlayer();
                }
            }
        }
        return musicPlayer;
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void play(Guild guild, TextChannel channel, Member member,
                             GuildMusicManager musicManager, AudioTrack track) {
        Accessibility.join(guild, channel, member);
        musicManager.scheduler.queue(track);
        getTracksList(channel);
    }

    public void loadAndPlay(Guild guild, TextChannel channel, Member member, String userInput) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        final String url;

        if (!UrlValidator.getInstance().isValid(userInput)) {
            try {
                url = youtube.getUrlBySearchTerms(userInput);
            } catch (IOException e) {
                log.error("Error calling youtube data api. Error:{}", e.getMessage());
                return;
            }
        } else {
            url = userInput;
        }

        playerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(guild, channel, member, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> curSongSearchList = playlist.getTracks();
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = curSongSearchList.get(0);
                }

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title
                        + " (first track of playlist " + playlist.getName() + ")").queue();

                if (curSongSearchList.size() > 1) {
                    if (curSongSearchList.remove(firstTrack)) {
                        Vector tempVector = new Vector<>();
                        tempVector.addAll(curSongSearchList);
                        musicManager.scheduler.queueAll(tempVector);
                    }
                }
                play(guild, channel, member, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + userInput).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void skipTrack(TextChannel channel, boolean isRepeat) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        if (musicManager.scheduler.getCurrentPlayingTrack() == null) {
            channel.sendMessage("You have no more songs to skip, go skip yourself 😎").queue();
         }else {
            musicManager.scheduler.nextTrack(isRepeat);
            if (musicManager.scheduler.getCurrentPlayingTrack() == null) {
                channel.sendMessage("That was your last song man. Bye 😎").queue();
            } else if (!isRepeat) {
                channel.sendMessage("Skipped to next track.").queue();
                getTracksList(channel);
            }
        }
    }

    public void skipTrackAtIndex(TextChannel channel, String songIndexStr) {
       if(StringUtils.isNumeric(songIndexStr)){
            int songIndex = Integer.parseInt(songIndexStr) - 1;
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            AudioTrack removedTrack = musicManager.scheduler.removeTrackFromCurrentQueueAtIndex(songIndex);
            if(removedTrack != null){
                channel.sendMessage("⏭ Removed "+removedTrack.getInfo().title).queue();
                getTracksList(channel);
            }else{
                channel.sendMessage("No Such Song Track Id To Be Removed Bruhhh! 😎").queue();
            }
        }else{
            channel.sendMessage("Invalid Song Track Id Given Bruhhh! 😎").queue();
        }
    }

    public void stopAllTracks(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.emptyAllTrack();
        channel.sendMessage("Stopping all tracks.").queue();
    }

    public void pauseTrack(TextChannel channel, boolean isPause) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.setPlayerPause(isPause);
        if(isPause) {
            channel.sendMessage("You mute me? Watch out!").queue();
        }else {
            channel.sendMessage("Finally you remember me! 😎").queue();
            getTracksList(channel);
        }
    }

    public void setVolume(TextChannel channel, String volume) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        if (StringUtils.isBlank(volume)) {
            int currentVolume = musicManager.scheduler.getPlayerVolume();
            channel.sendMessage("😎 I am at volume: "+currentVolume+
                    " (throw me a number between 0 - 150 to change my volume)").queue();
        } else if (StringUtils.isNumeric(volume) && hasHitVolumeLimit(volume)) {
            musicManager.scheduler.setPlayerVolume(Integer.parseInt(volume));
            channel.sendMessage("😎 Setting myself volume: "+volume).queue();
        } else {
            channel.sendMessage("Do you know what is a number? Stopping giving me "+volume).queue();
        }
    }

    private boolean hasHitVolumeLimit(String volume) {
        return Integer.valueOf(volume) >= 0 && Integer.valueOf(volume) <= 150;
    }

    public void getTracksList(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioTrack curPlayingTrack = musicManager.scheduler.getCurrentPlayingTrack();
        if(curPlayingTrack != null){
            Vector<AudioTrack> curAudioTrackQueue = musicManager.scheduler.getCurrentQueuedTracksList();
            displayAllQueuedTracksList(channel, curPlayingTrack, curAudioTrackQueue);
        }else{
            channel.sendMessage("Your Queue Is Empty, Fill Me Up 😎").queue();
        }
    }

    private void displayAllQueuedTracksList(TextChannel channel, AudioTrack curPlayingTrack, Vector<AudioTrack> curAudioTrackQueue) {
        EmbedBuilder curPlayingEmbedBuilder = getNowPlaying(curPlayingTrack);
        String returnedListStr = constructSongListDetails(channel,curAudioTrackQueue,1024);
        curPlayingEmbedBuilder.addField("⏯ Queue", (!StringUtils.isBlank(returnedListStr) ?
                returnedListStr : "Your Queue Is Empty, Fill Me Up \uD83D\uDE0E") , false);
        curPlayingEmbedBuilder.setColor(Color.RED);
        channel.sendMessage(curPlayingEmbedBuilder.build()).queue();
    }

    private EmbedBuilder getNowPlaying(AudioTrack curPlayingTrack) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("😎 Astronomia Music 😎");
        embedBuilder.setAuthor("NOW PLAYING", curPlayingTrack.getInfo().uri, null);
        embedBuilder.addField("🔊 Title", MessageHelper.convertTextToURL(curPlayingTrack.getInfo().title,
                curPlayingTrack.getInfo().uri), true);
        embedBuilder.addField("🎤 Singer", curPlayingTrack.getInfo().author, true);
        embedBuilder.addField("▶ Duration", (curPlayingTrack.getInfo().isStream)
                ? "Stream" : getTimeStamp(curPlayingTrack.getDuration()), true);
        return embedBuilder;
    }

    private String getTimeStamp(long curDuration) {
        String curTrackMinutes = Long.toString((curDuration / (1000*60)) % 60);
        String curTrackSeconds = Long.toString((curDuration / 1000) % 60);
        StringBuilder sb = new StringBuilder();
        if (curTrackMinutes.length() == 1) {
            curTrackMinutes ="0"+curTrackMinutes;
        }
        if (curTrackSeconds.length() == 1) {
            curTrackSeconds ="0"+curTrackSeconds;
        }
        return sb.append(curTrackMinutes).append(":").append(curTrackSeconds).toString();
    }

    public void pushSongPosition(TextChannel channel, int songCurrentPosition, int songNewPosition) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioTrack removedTrack = musicManager.scheduler.removeTrackFromCurrentQueueAtIndex(songCurrentPosition-1);
        if (removedTrack != null) {
            musicManager.scheduler.pushSelectedTrackToIndex(removedTrack,songNewPosition-1);
            channel.sendMessage("⏭ Pushing Song To Front of The Queue: "+removedTrack.getInfo().title).queue();
            getTracksList(channel);
        }else{
            channel.sendMessage("No Such Song Track Id To Be Removed Bruhhh! 😎").queue();
        }
    }

    public void repeatSong(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioTrack curPlayingTrack = musicManager.scheduler.getCurrentPlayingTrack();
        if (curPlayingTrack != null) {
            musicManager.scheduler.pushSelectedTrackToIndex(curPlayingTrack.makeClone(),0);
            this.skipTrack(channel, true);
            channel.sendMessage("🔁 Replaying "+curPlayingTrack.getInfo().title+" now!").queue();
        } else {
            channel.sendMessage("You mad bruh? There's no song playing currently for me to replay!").queue();
        }
    }

    private String constructSongListDetails(TextChannel channel, Vector<AudioTrack> curSongList, int textLimit) {
        int songCounter = 1;
        String prevListStr = "";
        StringBuilder songListBuilder = new StringBuilder();
        Iterator<AudioTrack> audioTrackIterator = curSongList.iterator();

        while (audioTrackIterator.hasNext()) {
            AudioTrack currentAudioTrackIt = audioTrackIterator.next();
            songListBuilder.append(songCounter)
                    .append(". ")
                    .append(MessageHelper.convertTextToURL(currentAudioTrackIt.getInfo().title,
                            currentAudioTrackIt.getInfo().uri))
                    .append("\n Duration: ")
                    .append(getTimeStamp(currentAudioTrackIt.getDuration()))
                    .append("\n\n");
            if (songListBuilder.toString().length() <= (textLimit-30)) {
                //Max length in Field/Description but giving 24 Char for below Message To Prevent OutOfRange error
                songCounter++;
                prevListStr = songListBuilder.toString();
            } else {
                break;
            }
        }
        if (StringUtils.isBlank(songListBuilder.toString())) {
            return null;
        } else if(songListBuilder.toString().length() <= textLimit) {
            return songListBuilder.toString();
        } else {
            prevListStr += "And "+(curSongList.size() - (songCounter-1))+" Other Songs In The List! 😎";
           return prevListStr;
        }
    }

    public void getSongHistory(TextChannel channel) {
        EmbedBuilder curPlayingEmbedBuilder = new EmbedBuilder();
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        Vector<AudioTrack> curSongHistoryList = musicManager.scheduler.getSongHistory();
        curPlayingEmbedBuilder.setAuthor("😎 Your History of Recent 20 Songs Played 😎");
        String returnedListStr = constructSongListDetails(channel,curSongHistoryList,2048);
        curPlayingEmbedBuilder.setDescription(!StringUtils.isBlank(returnedListStr) ?
                returnedListStr : "You have not listen to any song yet!");
        curPlayingEmbedBuilder.setColor(Color.GREEN);
        channel.sendMessage(curPlayingEmbedBuilder.build()).queue();
    }
}
