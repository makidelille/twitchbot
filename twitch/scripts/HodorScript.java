package twitch.scripts;

import twitch.bots.Bot;
import twitch.util.RandomText;


public class HodorScript extends Script{

    @Override
    public boolean execute(Bot bot,String channel, String sender, String msg) {
        if(msg.startsWith("!")) return false;
        else if(!msg.toLowerCase().contains("hodor")) return false;
        bot.sendMeText(channel, "HODOR", sender, RandomText.getRandomColor());
        return true;
    }
   
   
    
}
