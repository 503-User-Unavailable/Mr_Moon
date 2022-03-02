package Mr_Moon.CommandCenter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandInterface {
    void execute(MessageReceivedEvent event);  

    String help(String prefix);
}
