package Mr_Moon.CommandCenter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class join implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        //prereq variables for the code
        Guild guild = event.getGuild();
        AudioManager manager = guild.getAudioManager();
        Member member= event.getMember();
        MessageChannel channel = event.getChannel();
        AudioChannel voiceChannel = member.getVoiceState().getChannel();


        if (voiceChannel == null){
            channel.sendMessage("You must be in a voice channel for me to do this command").queue();
        }
        else{
            if (manager.getConnectedChannel() == voiceChannel){
                channel.sendMessage("I'm already here!").queue();;
            }
            manager.openAudioConnection(voiceChannel);
        }
        
    }

    @Override
    public String help(String prefix) {
        return "**Join**: Enters the user's voice channel ( " + prefix + "join )";
    }
    
}
