
package twitch.data.userData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import twitch.Main;


public class UserRights {
    
    public static enum AccessRight {
        ND, BANNED, WARNED, VIEWER, OP, MASTER;
        
        protected static AccessRight getUpperAccessRight(AccessRight curent) {
            switch (curent) {
                case MASTER:
                    return MASTER;
                case OP:
                    return MASTER;
                case VIEWER:
                    return OP;
                case WARNED:
                    return VIEWER;
                case BANNED:
                    return WARNED;
                default:
                    return ND;
            }
        }
        
        protected static AccessRight getDownerAccessRight(AccessRight curent) {
            switch (curent) {
                case MASTER:
                    return OP;
                case OP:
                    return VIEWER;
                case VIEWER:
                    return WARNED;
                case WARNED:
                    return BANNED;
                case BANNED:
                    return BANNED;
                default:
                    return ND;
            }
        }
        
        public static boolean isOp(AccessRight a) {
            return a.equals(OP) || a.equals(MASTER);
        }

        public static boolean isMaster(AccessRight accessRight) {
            return accessRight.equals(MASTER);
        }
    }
    
    public HashMap<String, AccessRight> userAccesMap;
    
    public UserRights() {
        userAccesMap = new HashMap<String, UserRights.AccessRight>();
        userAccesMap.put(Main.MASTER.toLowerCase(), AccessRight.MASTER);
    }
    
    public void addUser(String user) {
        userAccesMap.put(user.toLowerCase(), AccessRight.VIEWER);
    }

    public void removeOp(String modo) {
        userAccesMap.put(modo.toLowerCase(), AccessRight.VIEWER);
    }

    public void addOp(String modo) {
       if(userAccesMap.containsKey(modo.toLowerCase())){
           if(userAccesMap.get(modo.toLowerCase()).equals(AccessRight.MASTER)) return;
       }
        
        userAccesMap.put(modo.toLowerCase(), AccessRight.OP);
    }
    
    
    public void addMaster(String user) {
        userAccesMap.put(user.toLowerCase(), AccessRight.MASTER);
    }
    
    public void addStreamer(String channel) {
        userAccesMap.put(channel.toLowerCase(), AccessRight.MASTER);
    }
    
    
    public void setUserRight(String user,AccessRight right) {
       userAccesMap.put(user.toLowerCase(), right);
    }
    
    public AccessRight getAccessRight(String userToCompare) {
        if (!userAccesMap.containsKey(userToCompare.toLowerCase())) return AccessRight.ND;
        return userAccesMap.get(userToCompare.toLowerCase());
    }
    
    public ArrayList<String> getOpUsers() {
        ArrayList<String> modo = new ArrayList<String>();
        Iterator<Entry<String, AccessRight>> it = userAccesMap.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, AccessRight> entry = it.next();
            if(entry.getValue().equals(AccessRight.OP)) modo.add(entry.getKey());
        }
        return modo;
    }
    
    public ArrayList<String> getMasterUsers() {
        ArrayList<String> master = new ArrayList<String>();
        Iterator<Entry<String, AccessRight>> it = userAccesMap.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, AccessRight> entry = it.next();
            if(entry.getValue().equals(AccessRight.MASTER)) master.add(entry.getKey());
        }
        return master;
    }

}
