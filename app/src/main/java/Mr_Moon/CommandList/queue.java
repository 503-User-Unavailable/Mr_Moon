package Mr_Moon.CommandList;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class queue implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = guildMusicManager.scheduler.queue;

        if (queue.isEmpty()) {
            channel.sendMessage("The queue is currently empty").queue();
            return;
        }

        //page selections
        String[] msg = event.getMessage().getContentRaw().split(" ");
        int page;
        if (msg.length < 2) {page = 1;}
        else {page = Integer.parseInt(msg[1]);}
        int totalPages = queue.size()/10;
        if (queue.size() % 10 != 0) {totalPages++;}

        final List<AudioTrack> trackList = new ArrayList<>(queue);
        AudioTrack audioTrack = guildMusicManager.audioPlayer.getPlayingTrack();
        Member memberUserField = (Member) audioTrack.getUserData();
        String loop = (guildMusicManager.scheduler.looping) ? "ON" : "OFF";
        String fullLoop = (guildMusicManager.scheduler.fullLoop) ? "ON" : "OFF";
        String requestedBy = memberUserField.getUser().getName();
        if (memberUserField.getNickname() != null) {requestedBy = memberUserField.getNickname() + " (" + requestedBy + ")";}

        //calculating the total time of songs in queue/player
        Long totalTime = audioTrack.getDuration() - audioTrack.getPosition();
        for (AudioTrack elt : queue) {totalTime += elt.getDuration();}
        
        EmbedBuilder embed = new EmbedBuilder()
            .setFooter("page " + page + "/" + totalPages +" | Loop: " + loop + " | Queue Loop: "+ fullLoop,event.getAuthor().getAvatarUrl())
            .setColor(new Color(119,255,51))
            .setTitle("Queue for " + event.getGuild().getName())
            .setDescription("__Now Playing:__ \n [" + audioTrack.getInfo().title + "]("
                + audioTrack.getInfo().uri + ") | `"
                + formatTime(audioTrack.getDuration())
                + " Requested by: " + requestedBy +"`");
                
        for (int i = 0; i <= 9; i++) {
            int offset = (10 *(page - 1));
            if (trackList.size() > (i + offset)) {

                AudioTrack track = trackList.get(i+offset);
                AudioTrackInfo info = track.getInfo();
                Member innerMemberUserField = (Member) track.getUserData();
                String innerRequestedBy = innerMemberUserField.getUser().getName();
                if (innerMemberUserField.getNickname() != null){innerRequestedBy = innerMemberUserField.getNickname() + " (" + innerRequestedBy + ")";}
                
                if (i == 0) {
                    embed.addField("","__Up Next__\n" + "`" + ((i+1) + offset) + ".` [" + info.title + "](" + info.uri + ") | `"
                     + formatTime(track.getDuration()) + " Requested by: " + innerRequestedBy +"`",false);
                }
                else {
                    embed.addField("", "`" + ((i+1) + offset) + ".` [" + info.title + "](" + info.uri + ") | `" 
                     + formatTime(track.getDuration()) + " Requested by: " + innerRequestedBy +"`",false); 
                } 
            }
        }
        embed.addField(queue.size() + " songs in queue | " + formatTime(totalTime) + " total length", "", false);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String help(String prefix) {
        return "**Queue**: Shows the current song queue (" + prefix + "{queue/q})";
    }

    private String formatTime(long timeInMillis) {
        long hours = (timeInMillis / TimeUnit.HOURS.toMillis(1));
        long minutes = (timeInMillis / TimeUnit.MINUTES.toMillis(1)) - (60 * hours);
        long seconds = (timeInMillis / TimeUnit.SECONDS.toMillis(1)) - (60 * minutes) - (3600 * hours);

        if (hours == 0) {return String.format("%01d:%02d", minutes, seconds);}
        else {return String.format("%01d:%02d:%02d", hours, minutes, seconds);}
    }
}