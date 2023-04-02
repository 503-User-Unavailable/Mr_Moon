package Mr_Moon.CommandList;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class helpCommand implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        //it doesn't do anything
    }

    @Override
    public String help(String prefix) {
        return "**Help**: Displays this message ( " + prefix + "{help/h} ) ";
    }
    
}
