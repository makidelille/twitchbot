
package twitch.data.userData;

import java.util.HashMap;

import twitch.bots.Bot;
import twitch.util.RandomText;


public class TimeoutMap {
    
    public static HashMap<String, TimeoutMap> streamMap = new HashMap<String, TimeoutMap>();
    private HashMap<String, Byte> timeoutMap;
    /**
     * correspond aux diff�rents temps de timeout :
     * 10s,1m,5m,10m,30m,1h,6h,24h,7d
     */
    private int[] timeoutTime = { 10, 60, 300, 600, 1_800, 3_600, 6 * 3_600, 24 * 3_6000, 24 * 7 * 3_600 };
    
    public TimeoutMap(String stream) {
        if (!streamMap.containsKey(stream.toLowerCase())) streamMap.put(stream.toLowerCase(), this);
        timeoutMap = new HashMap<String, Byte>();
    }
    
    public void applyTimeout(Bot bot, String channel, String user) {
        if (!timeoutMap.containsKey(user.toLowerCase())) {
            timeoutMap.put(user.toLowerCase(), (byte) 0);
        } else {
            byte times = timeoutMap.get(user.toLowerCase()).byteValue();
            times = (times < timeoutTime.length - 1 ? times : times++);
// TODO remove   timeoutMap.put(user.toLowerCase(), times++);
            System.out.println(times);
        }
        try {
            int timeouttime = timeoutTime[timeoutMap.get(user.toLowerCase())];
            String warningMsg = RandomText.getTimeoutMsg(timeouttime);
            bot.sendText(channel, warningMsg, user);
            bot.sendTimeout(user, timeouttime);
        } catch (IndexOutOfBoundsException e) {
            String banMsg = "";
            bot.sendText(channel, banMsg, user);
        }
    }
}