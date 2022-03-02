package Mr_Moon.CommandCenter;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class rewind implements CommandInterface{
    
    @Override
    public void execute(MessageReceivedEvent event) {
        //prereqs
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        AudioTrack track = Audioplayer.getPlayingTrack();
        
        //time Shenanigins
        long currentTime = track.getPosition();
        String[] msg = event.getMessage().getContentRaw().split(" ");
        long timeMod = Long.parseLong(msg[1])*1000;
        track.setPosition(currentTime-timeMod);
        event.getChannel().sendMessage(":musical_note: **Set position to ** `" + formatTime(track.getPosition()) + "` :rewind:").queue();
    }

    @Override
    public String help(String prefix) {
        return "**Rewind**: Rewinds the playing music (" + prefix + "rewind [# in seconds])";
    }

    //easy formating for time
    private String formatTime(long timeInMillis){
        final long hours = (timeInMillis / TimeUnit.HOURS.toMillis(1));
        final long minutes = (timeInMillis / TimeUnit.MINUTES.toMillis(1)) - (60 * hours);
        final long seconds = (timeInMillis / TimeUnit.SECONDS.toMillis(1)) - (60 * minutes) - (3600 * hours);

        if (hours == 0){
            return String.format("%01d:%02d", minutes, seconds);
        }
        else{
            return String.format("%01d:%02d:%02d", hours, minutes, seconds);
        }
    }
}
