package Mr_Moon;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.MessageAction;


public class Commands extends ListenerAdapter {
    public static String prefix = "~";
    
    public void onMessageReceived(MessageReceivedEvent event){
        //splitting up the text recieved into individual words, then using the first word to check for bot commands..
        String[] msg = event.getMessage().getContentRaw().split(" ");
        String activation = msg[0];
        Guild guild = event.getGuild();

        //if the first word has the prefix, starting out with a "~"
        //does not activate on a bot's message to prevent infinite loops
        if (activation.contains(prefix) && !(event.getAuthor().isBot())){

            //the channel and user the command has come in from 
            MessageChannel channel = event.getChannel();
            Member member= event.getMember();

            //ping pong, basic "is it up" command
            if (activation.equals(prefix + "ping")){
                channel.sendMessage("Pong!").queue();
                
            }

            //testing the abilities to get information about the user
            //now sends the users info in this way: nickname (discord name)
            if (activation.equals(prefix + "whoami")){ 
                channel.sendMessage("You are: "+ member.getNickname() + " (" + member.getUser().getName() + ")").queue();
            }


            //voice commands
            //setting up variables that are used in multiple commands here first
            AudioManager manager = guild.getAudioManager();
            VoiceChannel voiceChannel = member.getVoiceState().getChannel();
            String searches = "";
            for (int i = 1; i < msg.length; i++){
                searches = searches + msg[i] + " ";
            }
            searches.trim();
            GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
            

            //simple join command, gets the bot into a voice channel (says if it is already here)
            if (activation.equals(prefix + "join") || activation.equals(prefix + "fuckon")){
                if (manager.getConnectedChannel() == voiceChannel){
                    channel.sendMessage("I'm already here!").queue();
                }
                manager.openAudioConnection(voiceChannel);
                
            }

            //makes the bot leave a voice channel (doesn't work if it isn't in a voice channel)
            if (activation.equals(prefix + "leave") || activation.equals(prefix + "fuckoff") || activation.equals(prefix + "shut")){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else{
                    guildMusicManager.scheduler.player.stopTrack();
                    guildMusicManager.scheduler.queue.clear();
                    manager.closeAudioConnection();
                }
            }

            //will play the given link or search from youtube
            if (activation.equals(prefix + "play") || activation.equals(prefix + "p")){
                //checks if you need to search for a video
                if (!searches.contains("https://")){
                    searches = "ytsearch:" + searches;
                }
                else{searches = msg[1];}

                //makes sure that the sender is in a voice channel
                if (voiceChannel == null){
                    channel.sendMessage("You must be in a voice channel for me to do this command").queue();
                }
                //makes sure that there is an actual search
                else if (searches == ""){
                    channel.sendMessage("hey, you might wanna choose something...").queue();
                }
                else {
                    manager.openAudioConnection(voiceChannel);
                    PlayerManager.getInstance().loadAndPlay((TextChannel) channel, searches);
                }
            }

            if ((activation.equals(prefix + "skip")) || (activation.equals(prefix + "s"))){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else if (Audioplayer.getPlayingTrack() == null){
                    channel.sendMessage("There is no track to skip").queue();
                }
                else{
                    guildMusicManager.scheduler.nextTrack();
                    channel.sendMessage("Skipping current track").queue();
                }
            }

            if (activation.equals(prefix + "clear")){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else{
                    guildMusicManager.scheduler.player.stopTrack();
                    guildMusicManager.scheduler.queue.clear();
                    channel.sendMessage("Queue has been fully cleared").queue();
                }
            }

            if (activation.equals(prefix + "np")){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else if (Audioplayer.getPlayingTrack() == null){
                    channel.sendMessage("I'm not playing anything");
                }
                else {
                    final AudioTrackInfo info = Audioplayer.getPlayingTrack().getInfo();
                    channel.sendMessageFormat("Now Playing '%s' by '%s' (link: <'%s'>)", info.title, info.author, info.uri).queue();
                }
            }
            
            if (activation.equals(prefix + "queue") || activation.equals(prefix + "q") ){
                final BlockingQueue<AudioTrack> queue = guildMusicManager.scheduler.queue;

                if (queue.isEmpty()){
                    channel.sendMessage("The queue is currently empty").queue();
                }
                else{
                    final int trackCount = Math.min(queue.size(), 20);
                    final List<AudioTrack> trackList = new ArrayList<>(queue);
                    MessageAction messageAction = channel.sendMessage("**Current Queue:**\n");
                    for (int i = 0; i < trackCount; i++){
                        final AudioTrack track = trackList.get(i);
                        final AudioTrackInfo info = track.getInfo();

                        messageAction.append('#')
                            .append(String.valueOf(i+1))
                            .append(" '")
                            .append(info.title)
                            .append(" by ")
                            .append(info.author)
                            .append("' ['")
                            .append(formatTime(track.getDuration()))
                            .append("']\n");
                    }
                    if (trackList.size() > trackCount){
                        messageAction.append("And")
                            .append(String.valueOf(trackList.size() - trackCount))
                            .append(" more...");
                    }
                    messageAction.queue();
                }
            }

            if(activation.equals(prefix + "loop") || activation.equals(prefix + "l")){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else if (Audioplayer.getPlayingTrack() == null){
                    channel.sendMessage("There is no track to loop").queue();
                }
                else if (voiceChannel == null){
                    channel.sendMessage("You must be in a voice channel for me to do this command").queue();
                }
                else{
                    //flips yes to no, no to yes
                    guildMusicManager.scheduler.looping = !guildMusicManager.scheduler.looping;
                    channel.sendMessage("Looping is now set to: " + guildMusicManager.scheduler.looping);
                }

            }
            
            if (activation.equals(prefix + "pause")){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else if (Audioplayer.getPlayingTrack() == null){
                    channel.sendMessage("There is no track to pause").queue();
                }
                else if (voiceChannel == null){
                    channel.sendMessage("You must be in a voice channel for me to do this command").queue();
                }
                else if (Audioplayer.isPaused()){
                    channel.sendMessage("Already paused the music, dude");
                }
                else{
                    Audioplayer.setPaused(true);
                    channel.sendMessage("The music has been paused");
                }
            }

            if (activation.equals(prefix + "resume")){
                if (manager.getConnectedChannel() == null){
                    channel.sendMessage("I'm not in a voice channel right now").queue();
                }
                else if (Audioplayer.getPlayingTrack() == null){
                    channel.sendMessage("There is no track to unpause").queue();
                }
                else if (voiceChannel == null){
                    channel.sendMessage("You must be in a voice channel for me to do this command").queue();
                }
                else if (!Audioplayer.isPaused()){
                    channel.sendMessage("The music isn't paused right now");
                }
                else{
                    Audioplayer.setPaused(false);
                    channel.sendMessage("The music has resumed");
                }
            }
        }
    }
    //for seeing how long until a song is in the queue
    private String formatTime(long timeInMillis){
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    };
}

