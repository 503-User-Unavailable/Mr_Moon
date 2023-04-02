package Mr_Moon.CommandList;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class clear implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        AudioManager manager = event.getGuild().getAudioManager();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (manager.getConnectedChannel() == null) {
            channel.sendMessage("I'm not currently in a voice channel").queue();
        }
        else {
            guildMusicManager.scheduler.player.stopTrack();
            guildMusicManager.scheduler.queue.clear();
            channel.sendMessage(":boom: ***Cleared...*** :stop_button:").queue();
        }
        
    }

    @Override
    public String help(String prefix) {
        return "**Clear**: Clears queue and playing song ( " + prefix + "clear )";
    }
}