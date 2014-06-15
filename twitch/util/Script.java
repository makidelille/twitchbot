package twitch.util;

import twitch.bots.Bot;


public abstract class Script {

    public Script(StreamerData stream){
        stream.addScript(this);
    }
    
    public boolean isMsgCmd(String msg){
        return msg.startsWith("!");
    }
    
    /**
     * 
     * @param channel
     * @param sender
     * @param msg
     * @return true if the script starts
     */
    public abstract boolean execute(Bot bot,String channel, String sender, String msg);

}


//TODO create link script and spam script