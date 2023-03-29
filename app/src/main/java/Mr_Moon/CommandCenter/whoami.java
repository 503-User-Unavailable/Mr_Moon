package Mr_Moon.CommandCenter;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class whoami implements CommandInterface{

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Member member= event.getMember();
        String hold = "You are: "+ member.getNickname() + " (" + member.getUser().getName() + ")";
        
        channel.sendMessage(hold).queue();
        
    }

    @Override
    public String help(String prefix) {
        return "**Whoami**: States users nickname and general name ( " + prefix + "whoami )";
    }
    
}
