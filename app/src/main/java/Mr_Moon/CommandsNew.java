package Mr_Moon;


import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import Mr_Moon.CommandCenter.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandsNew extends ListenerAdapter{
    //setting up a map of keys and values those keys lead to
    public LinkedHashMap<String,CommandInterface> Roledex = new LinkedHashMap<String,CommandInterface>();

    public CommandsNew() {
        
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
    }


    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event){
        //okay so lets go from here
    }

    public void onMessageReceived(@Nonnull MessageReceivedEvent event){
        //first test, if it's said by a bot, throw it away to prevent infinite loops
        if (event.getAuthor().isBot()) {return;}

        //the known prefix, will be used to test the prefix of events that come in
        String prefix = "~";

        //getting basic stuff about the message event (like the prefix, the command itself and any arguments)
        //this is used to make sure we want to try to find this key value pair from the map
        String[] msg = event.getMessage().getContentRaw().split(" ");
        String activation = msg[0].substring(1);
        
        //if all starting conditions aren't met, return
        if (!( (msg[0].contains(prefix)) && (Roledex.containsKey(activation)) && (event.getChannel().getName().contains("bot")) )) {return;}

        //creates and sends a string with all the keys and the ways their code works for the help command
        if (activation.equals("help") || activation.equals("h")){
            String help = "";
            for (var elt :Roledex.keySet()){
                //should weed out any duplicates
                if (!(help.contains(Roledex.get(elt).help(prefix)))){
                    help = help + Roledex.get(elt).help(prefix) + "\n";
                }
            }                    
            event.getChannel().sendMessage(help).queue();
        }
        else{
            //if it isn't the help command or change prefix command and it passed all the other tests, the code is then found and run
            CommandInterface starter = Roledex.get(activation);
            starter.execute(event);
        }
    }
}