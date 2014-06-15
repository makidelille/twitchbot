package twitch.scripts;

import twitch.bots.Bot;
import twitch.data.streamData.StreamerData;


public class XdScript extends Script {
    
    public XdScript(StreamerData stream) {
        super(stream);
    }

    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(isMsgCmd(msg) || sender.equalsIgnoreCase("makidelille")) return false;
        if(!msg.contains("Xd")) return false;
        bot.sendText(channel, "[@s],Toi aussi, tu sais pas les faire Xd", sender);
        return true;
    }
}
