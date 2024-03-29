package Mr_Moon.CommandList;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Mr_Moon.GuildMusicManager;
import Mr_Moon.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class fullLoop implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
        AudioManager manager = event.getGuild().getAudioManager();
        Member member = event.getMember();
        AudioChannel voiceChannel = member.getVoiceState().getChannel();

        if (manager.getConnectedChannel() == null) {
            channel.sendMessage("I'm not in a voice channel right now").queue();
        }
        else if (Audioplayer.getPlayingTrack() == null && guildMusicManager.scheduler.queue.isEmpty()) {
            channel.sendMessage("There are no tracks to loop").queue();
        }
        else if (voiceChannel == null) {
            channel.sendMessage("You must be in a voice channel for me to do this command").queue();
        }
        else {
            guildMusicManager.scheduler.fullLoop = !guildMusicManager.scheduler.fullLoop;
            String isIt = (guildMusicManager.scheduler.fullLoop) ? "**Enabled!**" : "**Disabled!**";
            channel.sendMessage(":repeat: " + isIt).queue();
        }
        
    }

    @Override
    public String help(String prefix) {
        return "**Full Loop**: loops current queue, including playing song (" + prefix + "fl)";
    }
}
