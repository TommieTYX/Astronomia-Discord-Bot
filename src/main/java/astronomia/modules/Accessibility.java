package astronomia.modules;

import astronomia.modules.musicplayer.MusicPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.managers.AudioManager;

public class Accessibility {
    public static void join(Guild guild, TextChannel channel, Member member, Interaction interaction) {
        VoiceChannel connectedChannel = member.getVoiceState().getChannel();

        if (!guild.getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL)) {
            // The bot does not have permission to see text / voice channel.
            interaction.reply("I cant see the text channel where you type the commands in, please give me permission!").setEphemeral(true).queue();
            return;
        }
        if (!guild.getSelfMember().hasPermission(connectedChannel, Permission.VOICE_CONNECT)) {
            // The bot does not have permission to view / join the voice channel. Don't forget the .queue()!
            interaction.reply("I do not have permissions to view / join the voice channel you are in!").setEphemeral(true).queue();
            return;
        }
        if (!guild.getSelfMember().hasPermission(connectedChannel, Permission.VOICE_SPEAK)) {
            // The bot does not have permission to speak in the voice channel. Don't forget the .queue()!
            interaction.reply("I do not have permissions to speak in the voice channel you are in!").setEphemeral(true).queue();
            return;
        }
        // Checks if they are in a channel -- not being in a channel means that the variable = null.
        if (connectedChannel == null) {
            interaction.reply("You are not connected to a voice channel!").setEphemeral(true).queue();
            return;
        }
        // Gets the audio manager
        AudioManager audioManager = guild.getAudioManager();
        if (audioManager.isAttemptingToConnect()) {
            interaction.reply("The bot is already trying to connect! Enter the chill zone!").setEphemeral(true).queue();
            return;
        }
        // Connects to the channel.
        audioManager.openAudioConnection(connectedChannel);
        // Only send once when first connect to channel
        if (!audioManager.isConnected()) {
            interaction.reply("HOLLAAA! I am connected to the voice channel! 😎").setEphemeral(true).queue();
        }
    }

    public static void leave(Guild guild, Interaction interaction) {
        // Gets the channel in which the bot is currently connected.
        VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();
        // Checks if the bot is connected to a voice channel.
        if (connectedChannel == null) {
            // Get slightly fed up at the user.
            interaction.reply("I am not connected to a voice channel!").setEphemeral(true).queue();
            return;
        } else {
            MusicPlayer.getInstance().stopAllTracks(guild, interaction);
        }
        // Disconnect from the channel.
        guild.getAudioManager().closeAudioConnection();
        // Notify the user.
        interaction.reply("I WILL BE BACK! 😎 Disconnected from the voice channel~").setEphemeral(true).queue();
    }
}
