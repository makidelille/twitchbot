
package twitch.data.userData;

import java.util.HashMap;

import twitch.bots.Bot;
import twitch.data.userData.UserRights.AccessRight;
import twitch.util.RandomText;


public class TimeoutMap {
    
    public static HashMap<String, TimeoutMap> streamMap = new HashMap<String, TimeoutMap>();
    private HashMap<String, Byte> timeoutMap;
    /**
     * correspond aux différents temps de timeout :
     * 10s,1m,5m,10m,30m,1h,6h,24h,7d
     */
    private int[] timeoutTime = { 10, 60, 300, 600, 1_800, 3_600, 6 * 3_600, 24 * 3_6000, 24 * 7 * 3_600 };
    
    public TimeoutMap(String stream) {
        if (!streamMap.containsKey(stream.toLowerCase())) streamMap.put(stream.toLowerCase(), this);
        timeoutMap = new HashMap<String, Byte>();
    }
    
    public void applyTimeout(Bot bot, String channel, String user) {
        byte times=0;
        if (!timeoutMap.containsKey(user.toLowerCase())) {
            timeoutMap.put(user.toLowerCase(), (byte) 0);
        } else {
            times = timeoutMap.get(user.toLowerCase()).byteValue();
            times = (times < timeoutTime.length - 1 ? times : times++);
            timeoutMap.put(user.toLowerCase(), times);
            System.out.println(times);
        }
        try {
            int timeouttime = timeoutTime[times];
            String warningMsg = RandomText.getTimeoutMsg(timeouttime);
            bot.sendText(channel, warningMsg, user);
            bot.sendTimeout(user, timeouttime);
            if(timeouttime > timeoutTime[2]) {
                bot.getStream().getUsers().setUserRight(user, AccessRight.WARNED);
            }
            if(timeouttime >= timeoutTime[7]){
                bot.getStream().getUsers().setUserRight(user, AccessRight.BANNED);
            }
        } catch (IndexOutOfBoundsException e) { // cannot be fire normaly
            String banMsg = "";
            bot.sendText(channel, banMsg, user);
        }
    }
}
