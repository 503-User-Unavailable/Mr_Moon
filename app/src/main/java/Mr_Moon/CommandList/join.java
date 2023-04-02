package Mr_Moon.CommandList;

import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class join implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        AudioManager manager = event.getGuild().getAudioManager();
        MessageChannel channel = event.getChannel();
        AudioChannel userVoiceChannel = event.getMember().getVoiceState().getChannel();

        if (userVoiceChannel == null) {
            channel.sendMessage("You must be in a voice channel for me to do this command").queue();
        }
        else {
            if (manager.getConnectedChannel() == userVoiceChannel) {
                channel.sendMessage("I'm already here!").queue();
            }
            manager.openAudioConnection(userVoiceChannel);
        }
    }

    @Override
    public String help(String prefix) {
        return "**Join**: Enters the user's voice channel ( " + prefix + "join )";
    }
}
