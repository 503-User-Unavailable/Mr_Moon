package Mr_Moon;


import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


//testing method, to get the errors to stay away
public class App {
    public String getGreeting() {
        return "Hello World!";
    }
  
    //main loop, starts the bot itself
    public static void main(String[] args) throws LoginException {
        
    //the start of the bot(adds in the ability to use commands from Commands.java)
    JDABuilder builder = JDABuilder.createDefault(args[0]).addEventListeners(new Commands());
    // Disable parts of the cache
    builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
    // Enable the bulk delete event
    builder.setBulkDeleteSplittingEnabled(false);
    // Disable compression (not recommended)
    builder.setCompression(Compression.NONE);
    // Set activity
    builder.setActivity(Activity.listening("To Discord!"));
    //actually building the bot
    builder.build();
    }
}