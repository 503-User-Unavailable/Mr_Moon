package Mr_Moon.CommandList;


import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class nowPlaying implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        AudioManager manager = event.getGuild().getAudioManager();
        AudioPlayer Audioplayer = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        AudioTrack track = Audioplayer.getPlayingTrack();
        String[] URLSplit = track.getInfo().uri.split("=");
        String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
    
        if (manager.getConnectedChannel() == null) {
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null) {
            channel.sendMessage("I'm not playing anything").queue();
        }
        else {
            Member memberUserField = (Member) track.getUserData();
            String requestedBy = memberUserField.getUser().getName();
            if (memberUserField.getNickname() != null) {requestedBy = memberUserField.getNickname() + " (" + requestedBy + ")";}

            String progressBar = "";
            Double progression = ((track.getPosition() * 1.0)/(track.getDuration() * 1.0)) * 30.0;
            Long converter = Math.round(progression);
            for (int i = 0; i < 29; i++) {
                if (i == converter) {progressBar += "🔘";}
                else {progressBar += "-";}
            }

            //embed built and sent
            EmbedBuilder wow = new EmbedBuilder()
                .setAuthor("Now Playing ♪","https://www.youtube.com/",event.getJDA().getSelfUser().getAvatarUrl())
                .setThumbnail(URL)
                .setColor(new Color(0, 86, 191, 1))
                .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
                .addField("", progressBar, false)
                .addField(" ", "`" + formatTime(track.getPosition()) + "/" + formatTime(track.getDuration()) + "`",false)
                .addField(" "," `Requested by:` " + requestedBy, false);
            channel.sendMessageEmbeds(wow.build()).queue();
        }
    }

    @Override
    public String help(String prefix) {
        return "**Now Playing**: Shows the currently playing song (" + prefix + "np)";
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
