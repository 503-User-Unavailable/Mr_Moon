package Mr_Moon.CommandCenter;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class pause implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        //prereq variables
        Guild guild = event.getGuild();
        AudioManager manager = guild.getAudioManager();
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        Member member= event.getMember();
        AudioChannel voiceChannel = member.getVoiceState().getChannel();

        if (manager.getConnectedChannel() == null){
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null){
            channel.sendMessage("There is no track to pause").queue();
        }
        else if (voiceChannel == null){
            channel.sendMessage("You must be in a voice channel for me to do this command").queue();
        }
        else if (Audioplayer.isPaused()){
            channel.sendMessage("Music already paused");
        }
        else{
            Audioplayer.setPaused(true);
            channel.sendMessage("The music has been paused");
        }
        
    }

    @Override
    public String help(String prefix) {
        return "**Pause**: Pauses the current song (" + prefix + "pause)";
    }
    
}
