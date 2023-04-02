package Mr_Moon;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    //when making any new object, you have to have the attributes of that object in this method at the start
    private final AudioPlayerSendHandler sendHandler;
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;

    //the object creation itself, adding attributes to the object
    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    //Gives the AudioPlayerSendHandler object we already created (in AudioPlayerSendHandler.java)
    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}