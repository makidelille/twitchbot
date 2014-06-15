
package twitch.data.streamData;

import java.util.HashMap;

import twitch.bots.Bot;


public class MakidelilleData extends StreamerData {
    
    public MakidelilleData() {
        super("makidelille");
    }
    
    public static HashMap<String, String> cfg;
    public static String                  resolution = "TODO";
    
    @Override
    protected void generateSubCmds() {
        cfg = new HashMap<String, String>();
        cfg.put("CPU", "I5 3570 @4.1GHz");
        cfg.put("RAM", "ram data");
        cfg.put("GPU", "gpu data");
    }
    
    @Override
    public void onSpecialCmd(Bot bot, String sender, String msg) {
    }
}
