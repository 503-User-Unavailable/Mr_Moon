package Mr_Moon.CommandList;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Mr_Moon.PlayerManager;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;


public class skip implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        AudioManager manager = event.getGuild().getAudioManager();
        AudioPlayer Audioplayer = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;

        if (manager.getConnectedChannel() == null) {
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null) {
            channel.sendMessage("There is no track to skip").queue();
        }
        else {
            Long time = Audioplayer.getPlayingTrack().getDuration();
            Audioplayer.getPlayingTrack().setPosition(time);
            channel.sendMessage(":fast_forward: ***Skipped*** :thumbsup:").queue();
        }
    }

    @Override
    public String help(String prefix) {
        return "**Skip**: Skips current song ( " + prefix + "{skip/s} )";
    }
}
