package twitch.scripts;

import java.util.HashMap;

import twitch.bots.Bot;
import twitch.data.streamData.StreamerData;
import twitch.data.userData.Timelog;


public class SpamScript extends Script{

    private static final int MAXMPM = 15;
    private HashMap<String, Timelog> userTime = new HashMap<String, Timelog>();;

    
    
    public SpamScript(StreamerData stream) {
        super(stream);
    }


    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(bot.getStream().isUserOp(sender)) return false;
        if(!userTime.containsKey(sender.toLowerCase())){
            userTime.put(sender.toLowerCase(), new Timelog());
            return false;
        }
        userTime.get(sender.toLowerCase()).addNewTime(System.currentTimeMillis());
        int mpm = userTime.get(sender.toLowerCase()).getMessagePerMinute();
        if(mpm > MAXMPM){
            bot.timeout(sender);
            return true;
        }
        
        
        return false;
    }
    
    
    



}
