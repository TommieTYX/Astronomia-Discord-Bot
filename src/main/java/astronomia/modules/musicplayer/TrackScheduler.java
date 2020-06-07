package astronomia.modules.musicplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Vector;

import static astronomia.constant.ApplicationConstants.DEFAULT_MUSIC_PLAYER_VOLUME;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
  private final AudioPlayer player;
  private final Vector<AudioTrack> queue;

  /**
   * @param player The audio player this scheduler uses
   */
  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    this.player.setVolume(DEFAULT_MUSIC_PLAYER_VOLUME);
    this.queue = new Vector<>();
  }

  /**
   * Add the next track to queue or play right away if nothing is in the queue.
   *
   * @param track The track to play or add to queue.
   */
  public void queue(AudioTrack track) {
    // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
    // something is playing, it returns false and does nothing. In that case the player was already playing so this
    // track goes to the queue instead.
    if (!player.startTrack(track, true)) {
      queue.add(track);
    }
  }

  /**
   * Start the next track, stopping the current one if it is playing.
   */
  public void nextTrack() {
    // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
    // giving null to startTrack, which is a valid argument and will simply stop the player.
    player.startTrack(queue.remove(0), false);
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
    if (endReason.mayStartNext) {
      nextTrack();
    }
  }

  /**
   * clear all tracks
   */
  public void emptyAllTrack() {
    player.destroy();
  }

  public void setPlayerPause(boolean isPause) {
    player.setPaused(isPause);
  }

  public int getPlayerVolume() {
    return player.getVolume();
  }

  public void setPlayerVolume(int volume) {
    player.setVolume(volume);
  }

  public AudioTrack getCurrentPlayingTrack() {
    return player.getPlayingTrack();
  }

  public Vector<AudioTrack> getCurrentQueuedTracksList() {
    return queue;
  }

  public AudioTrack removeTrackFromCurrentQueueAtIndex(int songIndex) {
    if(queue.size() > 0 && queue.size() > songIndex){
      return queue.remove(songIndex);
    }
    return null;
  }

  public void pushSelectedTrackToIndex(AudioTrack selectedTrack, int songNewIndex) {
    queue.insertElementAt(selectedTrack,songNewIndex);
  }
}