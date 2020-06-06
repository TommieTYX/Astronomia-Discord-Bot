package astronomia.feature.musicplayer;

import astronomia.feature.Accessibility;
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
import org.apache.maven.shared.utils.StringUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

public class MusicPlayer {

    private static AudioPlayerManager playerManager;
    private static Map<Long, GuildMusicManager> musicManagers;

    private static MusicPlayer musicPlayer = null;

    private MusicPlayer() {
        musicManagers = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();
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
    }

    public void loadAndPlay(Guild guild, TextChannel channel, Member member, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(guild, channel, member, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title
                        + " (first track of playlist " + playlist.getName() + ")").queue();

                play(guild, channel, member, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        if(musicManager.scheduler.getCurrentPlayingTrack() == null){
            channel.sendMessage("You have no more songs to skip, go skip yourself 😎").queue();
        }else {
            musicManager.scheduler.nextTrack();
            channel.sendMessage("Skipped to next track.").queue();
        }
    }

    public void skipTrackAtIndex(TextChannel channel, String songIndexStr) {
       if(StringUtils.isNumeric(songIndexStr)){
            int songIndex = Integer.parseInt(songIndexStr) - 1;
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            AudioTrack removedTrack = musicManager.scheduler.removeTrackFromCurrentQueueAtIndex(songIndex);
            if(removedTrack != null){
                channel.sendMessage("⏭ Removed "+removedTrack.getInfo().title).queue();
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

    private void displayAllQueuedTracksList(TextChannel channel, AudioTrack curPlayingTrack, Vector<AudioTrack> curAudioTrackQueue){
        int songCounter = 1;
        StringBuilder queueListBuilder = new StringBuilder();
        String prevQueueListStr = "";
        EmbedBuilder curPlayingEmbedBuilder = getNowPlaying(curPlayingTrack);
        Iterator<AudioTrack> audioTrackIterator = curAudioTrackQueue.iterator();

        while(audioTrackIterator.hasNext()){
            AudioTrack currentAudioTrackIt = audioTrackIterator.next();
            queueListBuilder.append(songCounter)
                    .append(". ")
                    .append(MessageHelper.convertTextToURL(currentAudioTrackIt.getInfo().title,
                            currentAudioTrackIt.getInfo().uri))
                    .append("\n Duration: ")
                    .append(getTimeStamp(currentAudioTrackIt.getDuration()))
                    .append("\n\n");
            if(queueListBuilder.toString().length() <= 1000) {
                //Max length in Field is 1024 but giving 24 Char for below Message To Prevent OutOfRange error
                songCounter++;
                prevQueueListStr = queueListBuilder.toString();
            }else{
                break;
            }
        }

        if(StringUtils.isBlank(queueListBuilder.toString())){
            curPlayingEmbedBuilder.addField("⏯ Queue", "Your Queue Is Empty, Fill Me Up 😎", false);
        }else if(queueListBuilder.toString().length() <= 1024){
            curPlayingEmbedBuilder.addField("⏯ Queue", queueListBuilder.toString(), false);
        }else{
            prevQueueListStr += "And "+(curAudioTrackQueue.size() - songCounter)+" Other Songs In Queue! 😎\n\n";
            curPlayingEmbedBuilder.addField("⏯ Queue", prevQueueListStr, false);
        }
        curPlayingEmbedBuilder.setColor(Color.RED);
        channel.sendMessage(curPlayingEmbedBuilder.build()).queue();
    }

    private EmbedBuilder getNowPlaying(AudioTrack curPlayingTrack){
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

    private String getTimeStamp(long curDuration){
        String curTrackMinutes = Long.toString((curDuration / (1000*60)) % 60);
        String curTrackSeconds = Long.toString((curDuration / 1000) % 60);
        StringBuilder sb = new StringBuilder();
        if(curTrackMinutes.length() == 1){
            curTrackMinutes ="0"+curTrackMinutes;
        }
        if(curTrackSeconds.length() == 1){
            curTrackSeconds ="0"+curTrackSeconds;
        }
        return sb.append(curTrackMinutes).append(":").append(curTrackSeconds).toString();
    }
}
