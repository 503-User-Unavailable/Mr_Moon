package Mr_Moon.CommandCenter;


import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class nowPlaying implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
    //prereq variables
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        AudioManager manager = guild.getAudioManager();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        AudioTrack track = Audioplayer.getPlayingTrack();
        String[] URLSplit = track.getInfo().uri.split("=");
        String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
    //making sure every condition is met
        if (manager.getConnectedChannel() == null){
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null){
            channel.sendMessage("I'm not playing anything");
        }
        else {
            //making and sending the embed
            Member memberUserField = (Member) track.getUserData();
            String requestedBy = memberUserField.getUser().getName();
            if (memberUserField.getNickname() != null){requestedBy = memberUserField.getNickname() + " (" + requestedBy + ")";}
            String progressBar = "";
            Double progression = ((track.getPosition() * 1.0)/(track.getDuration() * 1.0)) * 30.0;
            Long converter = Math.round(progression);
            for (int i = 0; i < 29; i++){
                if (i == converter){
                    progressBar += "🔘";
                }
                else{
                    progressBar += "-";
                }
            }
            EmbedBuilder wow = new EmbedBuilder();
            wow.setAuthor("Now Playing ♪","https://www.youtube.com/",event.getJDA().getSelfUser().getAvatarUrl());
            wow.setThumbnail(URL);
            wow.setColor(new Color(0, 86, 191, 1));
            wow.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")");
            wow.addField("", progressBar, false);
            wow.addField(" ", "`" + formatTime(track.getPosition()) + "/" + formatTime(track.getDuration()) + "`",false);
            wow.addField(" "," `Requested by:` " + requestedBy, false);
            channel.sendMessageEmbeds(wow.build()).queue();
        }
    }

    @Override
    public String help(String prefix) {
        return "**Now Playing**: Shows the currently playing song (" + prefix + "np)";
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
