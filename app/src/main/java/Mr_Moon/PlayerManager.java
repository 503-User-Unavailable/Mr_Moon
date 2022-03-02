package Mr_Moon;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;


public class PlayerManager {
    //when making any new object, you have to have the attributes of that object in this method at the start
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    //the object creation itself, adding attributes to the object
    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        //this adding of sources (I think) is so that the player manager knows where to take audio from
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    //(I really don't have any idea) but I think this might be just checking what GuildMusicManager the bot is using, and making a new one if there isn't one?
    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
        final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
        guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
        return guildMusicManager;
      });
    }

    //this method takes trackes given to it and puts them into the queue correctly
    public void loadAndPlay(TextChannel channel, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            //adding a single track
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessage("Adding to queue: '")
                .append(track.getInfo().title)
                .append("' by '")
                .append(track.getInfo().author)
                .queue();
            }
          
            //adding multiple tracks from a playlist
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
                int count = 0;
  
                for (AudioTrack track : tracks) {
                    //always do the first one, then it'll do the rest if it isn't search results
                    if ((count == 0) || (!playlist.isSearchResult())){
                        musicManager.scheduler.queue(track);

                        //as if it were the only one
                        if ((count == 0) && (playlist.isSearchResult())) {
                            channel.sendMessage("Adding to queue: '")
                            .append(track.getInfo().title)
                            .append("' by '")
                            .append(track.getInfo().author)
                            .append("'")
                            .queue();
                        }
                        count++;
                    }
                }
                if (!playlist.isSearchResult()){
                    channel.sendMessage("Adding to queue: '")
                    .append(String.valueOf(tracks.size()))
                    .append("' tracks from playlist '")
                    .append(playlist.getName())
                    .append("'")
                    .queue();
                }

                
            }

            //not adding a track because it couldn't find any
            @Override
            public void noMatches() {
                // Notify the user that we've got nothing
                channel.sendMessage("I found nothin...").queue();
            }
          
            //how tf did you make it fail????
            @Override
            public void loadFailed(FriendlyException throwable) {
                // Notify the user that everything exploded
                channel.sendMessage("Uhhhhhh, call repair man plz. ¯\\_(ツ)_/¯ ").queue();
            }
        });
    }

    //A quick little method to get the instance of the currect player manager, if there isn't one, it creates one.
    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
