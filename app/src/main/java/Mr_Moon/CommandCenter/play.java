package Mr_Moon.CommandCenter;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;


public class play implements CommandInterface{

    public static AudioTrack audioTrack = null;
    @Override
    public void execute(MessageReceivedEvent event) {
        //prereq variables
        String[] msg = event.getMessage().getContentRaw().split(" ");
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        AudioManager manager = guild.getAudioManager();
        VoiceChannel voiceChannel = member.getVoiceState().getChannel();
        
        String searches = "";
        for (int i = 1; i < msg.length; i++){
            searches = searches + msg[i] + " ";
        }
        searches.trim();

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
            PlayerManager.getInstance().loadAndPlay((TextChannel) channel, searches, event);
        }
    }

    @Override
    public String help(String prefix) {
        return "**Play**: Plays a song from youtube (" + prefix + "{play/p} [search terms or URL])";
    }
    
}
