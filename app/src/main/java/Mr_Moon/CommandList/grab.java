package Mr_Moon.CommandList;


import java.awt.Color;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class grab implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        AudioPlayer Audioplayer = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        AudioTrack track = Audioplayer.getPlayingTrack();
        String[] URLSplit = track.getInfo().uri.split("=");
        String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
        Member memberUserField = (Member) track.getUserData();
        String requestedBy = memberUserField.getUser().getName();
        if (memberUserField.getNickname() != null) {requestedBy = memberUserField.getNickname() + " (" + requestedBy + ")";}

        //embed built and sent
        EmbedBuilder wow = new EmbedBuilder()
            .setColor(new Color(80, 225, 172, 1))
            .setTitle("Song Saved! :musical_note:")
            .setThumbnail(URL)
            .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
            .addField(" ","`Length`  " + formatTime(track.getDuration()),false)
            .addField(" "," `Requested by:` " + requestedBy, false);
        sendMessage(event.getAuthor(), wow.build());
    }

    @Override
    public String help(String prefix) {
        return "**Grab**: Grabs current song, dm's user with it (" + prefix + "grab)";
    }

    //what'll be sending the dm to the user, not my code I just snagged it
    public void sendMessage(User user, @Nonnull MessageEmbed content) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(content)).queue();
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
