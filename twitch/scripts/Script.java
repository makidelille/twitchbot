
package twitch.scripts;

import twitch.bots.Bot;
import twitch.data.streamData.StreamerData;


public abstract class Script {
    
    public static void load() {
        new HodorScript(StreamerData.getStreamerData("monstro99"));
        new XdScript(StreamerData.common);
        new SpamScript(StreamerData.common);
        new HelloScript(StreamerData.common);
    }
    
    public Script(StreamerData stream) {
        stream.addScript(this);
    }
    
    public boolean isMsgCmd(String msg) {
        return msg.startsWith("!");
    }
    
    /**
     * @param channel
     * @param sender
     * @param msg
     * @return true if the script execute
     */
    public abstract boolean execute(Bot bot, String channel, String sender, String msg);
}
