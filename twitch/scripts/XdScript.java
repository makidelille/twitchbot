package twitch.scripts;

import twitch.bots.Bot;


public class XdScript extends Script {
    
    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(msg.startsWith("!") || sender.equalsIgnoreCase("makidelille")) return false;
        if(!msg.contains("Xd")) return false;
        bot.sendText(channel, "[@s],Toi aussi, tu sais pas les faire Xd", sender);
        return true;
    }
}
