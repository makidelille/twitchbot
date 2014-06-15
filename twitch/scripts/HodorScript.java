package twitch.scripts;

import twitch.bots.Bot;
import twitch.data.streamData.StreamerData;
import twitch.util.RandomText;


public class HodorScript extends Script{

    public HodorScript(StreamerData stream) {
        super(stream);
    }

    @Override
    public boolean execute(Bot bot,String channel, String sender, String msg) {
        if(isMsgCmd(msg)) return false;
        else if(!msg.toLowerCase().contains("hodor")) return false;
        bot.sendMeText(channel, "HODOR", sender, RandomText.getRandomColor());
        return true;
    }
   
   
    
}
