package Mr_Moon.CommandList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class shuffleQueue implements CommandInterface {

    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioManager manager = event.getGuild().getAudioManager();
        final BlockingQueue<AudioTrack> queue = guildMusicManager.scheduler.queue;

        if (event.getMember().getVoiceState().getChannel() == manager.getConnectedChannel()) {
            channel.sendMessage("cannot shuffle queue from outside voice channel").queue();
            return;
        }
        else if (queue.isEmpty()) {
            channel.sendMessage("Queue is currently empty...").queue();
            return;
        } 


        List<AudioTrack> trackList = new ArrayList<>(queue);
        Collections.shuffle(trackList);
        queue.clear();
        queue.addAll(trackList);
        
        channel.sendMessage("Shuffled successfully!").queue();
    }

    @Override
    public String help(String prefix) {
        return "**Shuffle**: Shuffles current queue ( " + prefix + "shuffle )";
    }
}
