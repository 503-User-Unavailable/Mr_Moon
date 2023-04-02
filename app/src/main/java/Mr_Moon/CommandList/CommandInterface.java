package Mr_Moon.CommandList;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandInterface {
    void execute(MessageReceivedEvent event);  

    String help(String prefix);
}
