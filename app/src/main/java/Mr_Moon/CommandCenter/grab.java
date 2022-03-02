package Mr_Moon.CommandCenter;


import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class grab implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        AudioTrack track = Audioplayer.getPlayingTrack();
        String[] URLSplit = track.getInfo().uri.split("=");
        String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
        
        //getting the embed made and sent out
        Member memberUserField = (Member) track.getUserData();
        String requestedBy = memberUserField.getUser().getName();
        if (memberUserField.getNickname() != null){requestedBy = memberUserField.getNickname() + " (" + requestedBy + ")";}
        EmbedBuilder wow = new EmbedBuilder();
        wow.setColor(new Color(80, 225, 172, 1));
        wow.setTitle("Song Saved! :musical_note:");
        wow.setThumbnail(URL);
        wow.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")");
        wow.addField(" ","`Length`  " + formatTime(track.getDuration()),false);
        wow.addField(" "," `Requested by:` " + requestedBy, false);
        sendMessage(event.getAuthor(),wow.build());
    }

    @Override
    public String help(String prefix) {
        return "**Grab**: Grabs current song, dm's user with it (" + prefix + "grab)";
    }

    //what'll be sending the dm to the user, not my code I just snagged it
    public void sendMessage(User user, MessageEmbed content) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(content)).queue();
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
