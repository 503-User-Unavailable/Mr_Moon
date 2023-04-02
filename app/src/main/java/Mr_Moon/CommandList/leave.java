package Mr_Moon.CommandList;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class leave implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        AudioManager manager = event.getGuild().getAudioManager();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (manager.getConnectedChannel() == null) {
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else {
            guildMusicManager.scheduler.player.stopTrack();
            guildMusicManager.scheduler.queue.clear();
            manager.closeAudioConnection();
        }
    }

    @Override
    public String help(String prefix) {
        return "**Leave**: Exits the current call ( " + prefix + "{leave/fuckoff/shut} )";
    }
}
