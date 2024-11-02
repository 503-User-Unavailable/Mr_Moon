package Mr_Moon.CommandList;

import java.util.LinkedHashMap;

import Mr_Moon.CommandHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class helpCommand implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        LinkedHashMap<String,CommandInterface> newRoledex = CommandHandler.getCommands();
        String prefix = "~";

        String help = "";
        for (var elt : newRoledex.keySet()) {
            //weeding out duplicates
            if (!(help.contains( newRoledex.get(elt).help(prefix) ))) {
                help = help + newRoledex.get(elt).help(prefix) + "\n";
            }
        }                    
        event.getChannel().sendMessage(help).queue();
        }

    @Override
    public String help(String prefix) {
        return "**Help**: Displays this message ( " + prefix + "{help/h} ) ";
    }
    
}
