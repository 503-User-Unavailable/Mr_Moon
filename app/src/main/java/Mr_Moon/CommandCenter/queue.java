package Mr_Moon.CommandCenter;

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
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class queue implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
    //prereq variables
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioTrack audioTrack = guildMusicManager.audioPlayer.getPlayingTrack();
        final BlockingQueue<AudioTrack> queue = guildMusicManager.scheduler.queue;
        String loop = (guildMusicManager.scheduler.looping) ?"✅" : "❌";
        String fullLoop = (guildMusicManager.scheduler.fullLoop) ?"✅" : "❌";
    //page selections
        String[] msg = event.getMessage().getContentRaw().split(" ");
        int page;
        if (msg.length < 2) {page = 1;}
        else {page = Integer.parseInt(msg[1]);}
        int totalPages = queue.size()/10;
        if (queue.size()%10 != 0) {totalPages++;}
    //calculating the total time of songs in queue/player
        Long curTime = audioTrack.getDuration() - audioTrack.getPosition();
        Long totalTime = curTime;
        for (AudioTrack elt : queue) {totalTime += elt.getDuration();}

        if (queue.isEmpty()){
            channel.sendMessage("The queue is currently empty").queue();
        }
        else{
            final List<AudioTrack> trackList = new ArrayList<>(queue);;
            Member memberUserField = (Member) audioTrack.getUserData();
            String requestedBy = memberUserField.getUser().getName();
            if (memberUserField.getNickname() != null){requestedBy = memberUserField.getNickname() + " (" + requestedBy + ")";}
            EmbedBuilder embed = new EmbedBuilder();
            
            embed.setFooter("page " + page + "/" + totalPages +" | Loop: " + loop + " | Queue Loop: "+ fullLoop,event.getAuthor().getAvatarUrl());
            embed.setColor(new Color(119,255,51));
            embed.setTitle("Queue for " + event.getGuild().getName());
            embed.setDescription("__Now Playing:__ \n [" + audioTrack.getInfo().title + "]("
             + audioTrack.getInfo().uri + ") | `"
             + formatTime(audioTrack.getDuration())
             + " Requested by: " + requestedBy +"`");
            for (int i = 0; i < 9; i++){
                int offset = (10 *(page - 1));
                if (trackList.size() > (i + offset)){
                    AudioTrack track = trackList.get(i+offset);
                    AudioTrackInfo info = track.getInfo();
                    Member innerMemberUserField = (Member) track.getUserData();
                    String innerRequestedBy = innerMemberUserField.getUser().getName();
                    if (innerMemberUserField.getNickname() != null){innerRequestedBy = innerMemberUserField.getNickname() + " (" + innerRequestedBy + ")";}
                    
                    if (i == 0){
                        embed.addField("","__Up Next__\n" + "`" + ((i+1) + offset) + ".` [" + info.title + "]("
                         + info.uri + ") | `"
                         + formatTime(track.getDuration())
                         + " Requested by: " + innerRequestedBy +"`",false);
                    }else{
                        embed.addField("", "`" + ((i+1) + offset) + ".` [" + info.title + "]("
                         + info.uri + ") | `"
                         + formatTime(track.getDuration())
                         + " Requested by: " + innerRequestedBy +"`",false); 
                    } 
                }
            }
            embed.addField(queue.size() + " songs in queue | " + formatTime(totalTime) + " total length", "", false);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public String help(String prefix) {
        return "**Queue**: Shows the current song queue (" + prefix + "{queue/q})";
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
