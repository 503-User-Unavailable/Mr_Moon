package Mr_Moon.CommandCenter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicHelp implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        //Nothing Happens Here
        
    }

    @Override
    public String help(String prefix) {
        return "```Music Commands```";
    }
    
}
