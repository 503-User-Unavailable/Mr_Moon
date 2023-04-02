package Mr_Moon.CommandList;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;


public class play implements CommandInterface {
    public static AudioTrack audioTrack = null;

    @Override
    public void execute(MessageReceivedEvent event) {
        String[] msg = event.getMessage().getContentRaw().split(" ");
        MessageChannel channel = event.getChannel();
        AudioManager manager = event.getGuild().getAudioManager();
        AudioChannel voiceChannel = event.getMember().getVoiceState().getChannel();
        
        String searches = "";
        for (int i = 1; i < msg.length; i++) {
            searches = searches + msg[i] + " ";
        }
        searches.trim();

        if (!searches.contains("https://")) {
            searches = "ytsearch:" + searches;
        }
        else{searches = msg[1];}

        if (voiceChannel == null) {
            channel.sendMessage("You must be in a voice channel for me to do this command").queue();
        }
        else if (searches == "") {
            channel.sendMessage("hey, you might wanna choose something...").queue();
        }
        else {
            manager.openAudioConnection(voiceChannel);
            PlayerManager.getInstance().loadAndPlay((TextChannel) channel, searches, event);
        }
    }

    @Override
    public String help(String prefix) {
        return "**Play**: Plays a song from youtube ( " + prefix + "{play/p} [search terms or URL] )";
    }
    
}
