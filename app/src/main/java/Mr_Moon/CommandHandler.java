package Mr_Moon;


import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import Mr_Moon.CommandList.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {
    //setting up a map of keys and values those keys lead to
    public LinkedHashMap<String,CommandInterface> Roledex = new LinkedHashMap<String,CommandInterface>();
    public String prefix = "~";

    public CommandHandler() {  
        Roledex.put("ThisCommandIsForGeneralHelp", new GeneralHelp());
        Roledex.put("help", new helpCommand());
        Roledex.put("h", new helpCommand());
        Roledex.put("ping", new ping());
        Roledex.put("whoami", new whoami());

        Roledex.put("ThisCommandIsForMusicHelp", new MusicHelp());
        //join commands
            Roledex.put("join", new join());
            Roledex.put("fuckon", new join());
        //leave commands
            Roledex.put("leave", new leave());
            Roledex.put("fuckoff", new leave());
            Roledex.put("shut", new leave());
        //play commands
            Roledex.put("play", new play());
            Roledex.put("p", new play());
        //skip commands
            Roledex.put("skip", new skip());
            Roledex.put("s", new skip());
        //queue commands
            Roledex.put("q", new queue());
            Roledex.put("queue", new queue());
        //loop commands
            Roledex.put("loop", new loop());
            Roledex.put("l", new loop());
        //full loop commands
            Roledex.put("fl",new fullLoop());
        Roledex.put("clear", new clear());
        Roledex.put("np", new nowPlaying());
        Roledex.put("pause", new pause());
        Roledex.put("resume", new resume());
        Roledex.put("grab", new grab());
        Roledex.put("forward", new forward());
        Roledex.put("rewind", new rewind());
        Roledex.put("remove", new remove());
        Roledex.put("shuffle", new shuffleQueue());
    }

    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        //TO-DO; 
    }

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        //testing if message is a command
        if (event.getAuthor().isBot()) {return;}
        String[] msg = event.getMessage().getContentRaw().split(" ");
        String activation = msg[0].substring(1);
        if (!( msg[0].contains(prefix) && Roledex.containsKey(activation) && event.getChannel().getName().contains("bot") )) {return;}
        else if (activation.equals("help") || activation.equals("h")) {
            String help = "";
            for (var elt : Roledex.keySet()) {
                //weeding out duplicates
                if (!(help.contains( Roledex.get(elt).help(prefix) ))) {
                    help = help + Roledex.get(elt).help(prefix) + "\n";
                }
            }                    
            event.getChannel().sendMessage(help).queue();
        }
        else {
            //run command
            Roledex.get(activation).execute(event);
        }
    }
}