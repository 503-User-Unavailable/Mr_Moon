package Mr_Moon;


import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


//testing method, to get the errors to stay away
public class App {
  
    //main loop, starts the bot itself
    public static void main(String[] args) throws LoginException {
    //the start of the bot(adds in the ability to use commands from Commands.java)
    JDABuilder builder = JDABuilder.createDefault(args[0]).addEventListeners(new CommandHandler());
    //show's messages contents
    builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
    // Disable parts of the cache (put here by default)
    builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
    // Disable the bulk delete event (put here by default)
    builder.setBulkDeleteSplittingEnabled(false);
    // Disable compression (not recommended)
    builder.setCompression(Compression.NONE);
    // Set activity (put here by default)
    builder.setActivity(Activity.watching("Discord"));
    //actually building the bot (put here by default)
    builder.build();
    }
}