package twitch.scripts;

import twitch.bots.Bot;
import twitch.util.RandomText;
import twitch.util.Script;
import twitch.util.StreamerData;


public class HodorScript extends Script{

    public HodorScript() {
        super(StreamerData.map.get("monstro99"));
    }

    @Override
    public boolean execute(Bot bot,String channel, String sender, String msg) {
        if(isMsgCmd(msg)) return false;
        else if(!msg.toLowerCase().contains("hodor")) return false;
        bot.sendMeText(channel, "HODOR", sender, RandomText.getRandomColor());
        return true;
    }
   
   
    
}
