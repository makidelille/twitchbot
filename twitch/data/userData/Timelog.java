package twitch.data.userData;

import java.util.ArrayList;



public class Timelog {
    
    private ArrayList<Long> timelastmsg = new ArrayList<Long>();
    public Timelog() {
        timelastmsg.add(System.currentTimeMillis());
    }
    
    public long getLastMessageTime() {
        return timelastmsg.get(timelastmsg.size()-1);
    }
    
    public int getTotalMessageInRow(int periodtime){
        if(timelastmsg.size() < 2) return 1;
        int i=0;
        int index = timelastmsg.size()-1;
        long timedif = 0;
        while(timedif < periodtime && index > 1){
            timedif =  timelastmsg.get(index) - timelastmsg.get(index -1);
            index--;
            i++;
        }
        return i;
    }
    
    //stocke les temps
    public void addNewTime(long time){
        timelastmsg.add(time);
    }

    public int getMessagePerMinute() {
        int total=0;
        for(int i =timelastmsg.size() -1 ; i > 0; i--){
            if(timelastmsg.get( timelastmsg.size()-1) - timelastmsg.get(i) > 60 * 1_000) break;
            total++;
        }
        return total;
    }
    
}
