package Mr_Moon;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

//the creation of the AudioPlayerSendHandler, used for JDA to connect to voice channels
public class AudioPlayerSendHandler implements AudioSendHandler {
  //when making any new object, you have to have the attributes of that object in this method at the start
  private final AudioPlayer audioPlayer;
  private final ByteBuffer buffer;
  private final MutableAudioFrame frame;
  private AudioFrame lastFrame;
  
  //the actual method itself, connecting the new class to its attributes
  public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
    this.buffer = ByteBuffer.allocate(1024);
    this.frame = new MutableAudioFrame();
    this.frame.setBuffer(buffer);
  }

  
  @Override
  public boolean canProvide() {
    lastFrame = audioPlayer.provide();
    return lastFrame != null;
  }

  @Override
  public ByteBuffer provide20MsAudio() {
    //may cause error, see if it works
    return ByteBuffer.wrap(lastFrame.getData());
  }

  @Override
  public boolean isOpus() {
    return true;
  }
}