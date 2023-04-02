package Mr_Moon.CommandList;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GeneralHelp implements CommandInterface {

    @Override
    public void execute(MessageReceivedEvent event) {
        //Nothing Happens Here
        
    }

    @Override
    public String help(String prefix) {
        return "```General Commands```";
    }
    
}
