package Mr_Moon.CommandList;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ping implements CommandInterface {

    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        channel.sendMessage("Pong!").queue();
    }

    @Override
    public String help(String prefix) {
        return "**Ping**: Simple 'is it up' command ( " + prefix + "ping )";
    }
}