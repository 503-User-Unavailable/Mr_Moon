package Mr_Moon.CommandCenter;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class clear implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        //prereq variables
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        AudioManager manager = guild.getAudioManager();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (manager.getConnectedChannel() == null){
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else{
            guildMusicManager.scheduler.player.stopTrack();
            guildMusicManager.scheduler.queue.clear();
            channel.sendMessage(":boom: ***Cleared...*** :stop_button:").queue();
        }
        
    }

    @Override
    public String help(String prefix) {
        return "**Clear**: Clears queue and playing song (" + prefix + "clear)";
    }
    
}