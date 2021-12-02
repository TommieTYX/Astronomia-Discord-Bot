package astronomia.modules;

import astronomia.modules.musicplayer.MusicPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Accessibility {
    public static void join(Guild guild, TextChannel channel, Member member) {
        if (!guild.getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) {
            // The bot does not have permission to join any voice channel. Don't forget the .queue()!
            channel.sendMessage("I do not have permissions to join a voice channel!").queue();
            return;
        }
        // Creates a variable equal to the channel that the user is in.
        VoiceChannel connectedChannel = member.getVoiceState().getChannel();
        // Checks if they are in a channel -- not being in a channel means that the variable = null.
        if (connectedChannel == null) {
            channel.sendMessage("You are not connected to a voice channel!").queue();
            return;
        }
        // Gets the audio manager
        AudioManager audioManager = guild.getAudioManager();
        if (audioManager.isAttemptingToConnect()) {
            channel.sendMessage("The bot is already trying to connect! Enter the chill zone!").queue();
            return;
        }
        // Connects to the channel.
        audioManager.openAudioConnection(connectedChannel);
        // Only send once when first connect to channel
        if (!audioManager.isConnected()) {
            channel.sendMessage("HOLLAAA! I am connected to the voice channel! 😎").queue();
        }
    }

    public static void leave(Guild guild, TextChannel channel) {
        // Gets the channel in which the bot is currently connected.
        VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();
        // Checks if the bot is connected to a voice channel.
        if (connectedChannel == null) {
            // Get slightly fed up at the user.
            channel.sendMessage("I am not connected to a voice channel!").queue();
            return;
        } else {
            MusicPlayer.getInstance().stopAllTracks(channel);
        }
        // Disconnect from the channel.
        guild.getAudioManager().closeAudioConnection();
        // Notify the user.
        channel.sendMessage("I WILL BE BACK! 😎 Disconnected from the voice channel~").queue();
    }
}
