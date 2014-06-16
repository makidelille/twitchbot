
package twitch.scripts;

import twitch.bots.Bot;


public abstract class Script {
        
    /**
     * @return true if the scripts ends the cmd logic
     */
    public abstract boolean execute(Bot bot, String channel, String sender, String msg);
}
