package Mr_Moon.CommandList;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;

public class forward implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        AudioTrack track = Audioplayer.getPlayingTrack();
    
        //extracting and forwarding time by specified amount
        long currentTime = track.getPosition();
        String[] msg = event.getMessage().getContentRaw().split(" ");
        long timeMod = Long.parseLong(msg[1])*1000;
        track.setPosition(currentTime+timeMod);

        event.getChannel().sendMessage(":musical_note: **Set position to ** `" + formatTime(track.getPosition()) + "` :fast_forward:").queue();
    }

    @Override
    public String help(String prefix) {
        return "**Fast Forward**: Advances playing music (" + prefix + "forward [# in seconds])";
    }

    private String formatTime(long timeInMillis) {
        long hours = (timeInMillis / TimeUnit.HOURS.toMillis(1));
        long minutes = (timeInMillis / TimeUnit.MINUTES.toMillis(1)) - (60 * hours);
        long seconds = (timeInMillis / TimeUnit.SECONDS.toMillis(1)) - (60 * minutes) - (3600 * hours);

        if (hours == 0) {
            return String.format("%01d:%02d", minutes, seconds);
        }
        else {
            return String.format("%01d:%02d:%02d", hours, minutes, seconds);
        }
    }
}
