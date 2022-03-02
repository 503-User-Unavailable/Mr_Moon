package Mr_Moon.CommandCenter;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class remove implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        //get the number to remove
        String[] msg = event.getMessage().getContentRaw().split(" ");
        int number = Integer.parseInt(msg[1]) - 1;
        //setting up the channel to send to as well as the music manager
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        Object[] arrayOfTracks = guildMusicManager.scheduler.queue.toArray();
        AudioTrack trackToName = (AudioTrack) arrayOfTracks[number];
        guildMusicManager.scheduler.queue.remove(arrayOfTracks[number]);
        channel.sendMessage(":white_check_mark: ***Removed*** `" + trackToName.getInfo().title + "` ").queue();
    }

    @Override
    public String help(String prefix) {
        return "**Remove**: removes the song from the selected # (" + prefix + "remove [number in queue])";
    }
    
}
