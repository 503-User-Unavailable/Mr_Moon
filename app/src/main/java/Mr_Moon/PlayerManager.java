package Mr_Moon;


import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


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
    public void loadAndPlay(TextChannel channel, String trackUrl, MessageReceivedEvent event){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            //adding a single track [ONLY WORKS FOR LINKS, YOUTUBE SEARCHES ARE CONSIDERED PLAYLISTS]
            @Override
            public void trackLoaded(AudioTrack track) {
            //inputing the user data in the track
                track.setUserData(event.getMember());
                
            //prereq variables
                String[] URLSplit = track.getInfo().uri.split("=");
                String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
                GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                AudioPlayer Audioplayer = guildMusicManager.audioPlayer;

                //time shenanagins
                Long totalTime = (long) 0;
                //getting the time of the playing track and adding it to totalTime
                if (!(Audioplayer.getPlayingTrack() == null)) {totalTime += (Audioplayer.getPlayingTrack().getDuration() - Audioplayer.getPlayingTrack().getPosition());}
                //going through every song in the queue and adding it to totalTime
                for (var elt : musicManager.scheduler.queue) {totalTime += elt.getDuration();}
                String totalTimeString = formatTime(totalTime);

            //if its empty AKA: first song played after silence
                if (Audioplayer.getPlayingTrack() == null){
                    channel.sendMessage("**Searching** :mag_right: `"+ trackUrl +"`\n"
                    + "**Playing** :notes: `" + track.getInfo().title + "` - Now!").queue();
                }
                else{
            //not the first song after silence
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("Added to queue", null, event.getAuthor().getAvatarUrl());
                    embed.setDescription("**[" + track.getInfo().title + "](" + track.getInfo().uri + ")**");
                    embed.addField("Channel", track.getInfo().author, true);
                    embed.addField("Song Duration", formatTime(track.getDuration()), true);
                    embed.addField("Time Until Playing", totalTimeString, true);
                    embed.setThumbnail(URL);
                    embed.setColor(new Color(255,25,25));
                    embed.addField("Position in queue", (musicManager.scheduler.queue.size() + 1) + "", false);
                    channel.sendMessageEmbeds(embed.build()).queue();
                }
                musicManager.scheduler.queue(track);
            }
          
            //adding multiple tracks from a playlist
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
                int count = 0;

                //this is for if it is an actual youtube playlist, not a search
                if (!playlist.isSearchResult()){
                //prereqs
                    GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                    AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
                    String queueWait = (Audioplayer.getPlayingTrack() == null) ? "Now" : (musicManager.scheduler.queue.size() + 1) + "";
                    String[] URLSplit = playlist.getTracks().get(0).getInfo().uri.split("=");
                    String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
                    //time shenanigins
                    Long totalTime = (long) 0;
                    if (!(Audioplayer.getPlayingTrack() == null)) {totalTime += (Audioplayer.getPlayingTrack().getDuration() - Audioplayer.getPlayingTrack().getPosition());}
                    for (var elt : musicManager.scheduler.queue) {totalTime += elt.getDuration();}
                    String totalTimeString = formatTime(totalTime);


                //embed being made and sent
                    EmbedBuilder embed2 = new EmbedBuilder();
                    embed2.setColor(new Color(255,25,25));
                    embed2.setAuthor("Added to queue", null, event.getAuthor().getAvatarUrl());
                    embed2.setDescription("**" + playlist.getName() + "**");
                    embed2.addField("Time Until Playing", totalTimeString, false);
                    embed2.setThumbnail(URL);
                    embed2.addField("Position in queue", queueWait, true);
                    embed2.addField("Enqueued", playlist.getTracks().size() + "", true);
                    channel.sendMessageEmbeds(embed2.build()).queue();
                }
                for (AudioTrack track : tracks) {
                    //always do the first one, then it'll do the rest if it isn't search results
                    if ((count == 0) || (!playlist.isSearchResult())){
                        if ((playlist.isSearchResult())) {

                        //prereqs
                            String[] URLSplit = track.getInfo().uri.split("=");
                            String URL = "https://i.ytimg.com/vi/" + URLSplit[1] + "/default.jpg";
                            String searchResults = trackUrl.split("ytsearch:")[1];
                            GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                            AudioPlayer Audioplayer = guildMusicManager.audioPlayer;
                            //time Shenanigins
                            Long totalTime = (long) 0;
                            if (!(Audioplayer.getPlayingTrack() == null)) {totalTime += (Audioplayer.getPlayingTrack().getDuration() - Audioplayer.getPlayingTrack().getPosition());}
                            for (var elt : musicManager.scheduler.queue) {totalTime += elt.getDuration();}
                            String totalTimeString = formatTime(totalTime);

                            //if its empty AKA: first song played after silence
                            if (Audioplayer.getPlayingTrack() == null){
                                channel.sendMessage("**Searching** :mag_right: `"+ searchResults +"`\n"
                                + "**Playing** :notes: `" + track.getInfo().title + "` - Now!").queue();
                            }
                            else{
                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setAuthor("Added to queue", null, event.getAuthor().getAvatarUrl());
                                embed.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")");
                                embed.addField("Channel", track.getInfo().author, true);
                                embed.addField("Song Duration", formatTime(track.getInfo().length), true);
                                embed.addField("Time Until Playing", totalTimeString, true);
                                embed.setThumbnail(URL);
                                embed.addField("Position in queue", (musicManager.scheduler.queue.size() + 1) + "", false);
                                embed.setColor(new Color(255,25,25));
                                channel.sendMessageEmbeds(embed.build()).queue();
                            }
                        }
                        //inputing the user data in with every track
                        track.setUserData(event.getMember());
                        musicManager.scheduler.queue(track);
                        //as if it were the only one
                        count++;
                    }
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

    //easy formating for time
    private String formatTime(long timeInMillis){
        final long hours = (timeInMillis / TimeUnit.HOURS.toMillis(1));
        final long minutes = (timeInMillis / TimeUnit.MINUTES.toMillis(1)) - (60 * hours);
        final long seconds = (timeInMillis / TimeUnit.SECONDS.toMillis(1)) - (60 * minutes) - (3600 * hours);

        if (hours == 0){
            return String.format("%01d:%02d", minutes, seconds);
        }
        else{
            return String.format("%01d:%02d:%02d", hours, minutes, seconds);
        }
    }
}