package twitch.scripts;

import java.util.ArrayList;

import twitch.bots.Bot;
import twitch.util.RandomText;


public class HelloScript extends Script {
    
    private ArrayList<String> users;
    
    public HelloScript() {
        users = new ArrayList<String>();
    }

    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(users.contains(sender.toLowerCase())) return false;
        users.add(sender.toLowerCase());
        bot.sendText(bot.getStreamChannel(), RandomText.getRanJoin(), sender);
        return false;
    }
}
