package Mr_Moon.CommandList;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class loop implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        AudioManager manager = event.getGuild().getAudioManager();
        AudioChannel voiceChannel = event.getMember().getVoiceState().getChannel();

        if (manager.getConnectedChannel() == null) {
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null) {
            channel.sendMessage("There is no track to loop").queue();
        }
        else if (voiceChannel == null) {
            channel.sendMessage("You must be in a voice channel for me to do this command").queue();
        }
        else {
            guildMusicManager.scheduler.looping = !guildMusicManager.scheduler.looping;
            String isIt = (guildMusicManager.scheduler.looping) ? "**Enabled!**" : "**Disabled!**";
            channel.sendMessage(":repeat_one: " + isIt).queue();
        } 
    }

    @Override
    public String help(String prefix) {
        return "**Loop**: loops current song (" + prefix + "{loop/l})";
    }
}
