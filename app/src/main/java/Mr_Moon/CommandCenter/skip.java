package Mr_Moon.CommandCenter;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;


public class skip implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        //prereq variables
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        AudioManager manager = guild.getAudioManager();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;


        if (manager.getConnectedChannel() == null){
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null){
            channel.sendMessage("There is no track to skip").queue();
        }
        else{
            guildMusicManager.scheduler.nextTrack();
            channel.sendMessage(":fast_forward: ***Skipped*** :thumbsup:").queue();
        }
        
    }

    @Override
    public String help(String prefix) {
        return "**Skip**: Skips current song (" + prefix + "{skip/s})";
    }
    
}
