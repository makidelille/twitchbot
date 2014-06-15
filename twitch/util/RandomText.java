
package twitch.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import twitch.Main;
import twitch.files.FileRWHelper;
import twitch.files.Lib;
import twitch.files.Lib.LibProcessing;


public class RandomText {
    
    // private static ArrayList<String> join, leave, random;
    // private static Path joinPath, leavePath, ranPath;
    private static final String[]                     libs   = { "join", "leave", "misc" };
    private static final String                       libDir = "lib";
    private static HashMap<String, ArrayList<String>> libStringsMap;
    private static HashMap<String, Path> libPathsMap;

    
    public static void load() {
        libStringsMap = new HashMap<String, ArrayList<String>>();
        libPathsMap = new HashMap<String, Path>();
        
        for (String lib : libs) {
            Lib temp = new Lib(libDir, lib, "txt");
            if (temp.loadFile()) {
                FileRWHelper.writeEndStringInFile(temp.getpath(), "#" + lib);
            }
            ArrayList<String> lines = temp.readAllLines();
            lines = LibProcessing.removeCommentedSection(lines, "#");
            libStringsMap.put(lib.toLowerCase(), lines);
            libPathsMap.put(lib.toLowerCase(), temp.getpath());
        }
 
    }
    
    public static String getRandomStringFromLib(String libName) {
        if (!libStringsMap.containsKey(libName.toLowerCase())) return "";
        ArrayList<String> lines = libStringsMap.get(libName.toLowerCase());
        if(lines.isEmpty()) return "";
        try {
            int ran = new Random().nextInt(lines.size());
            return lines.get(ran);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static boolean addStringToLib(String libName, String string) {
        if(!libPathsMap.containsKey(libName.toLowerCase())) return false;
        return FileRWHelper.writeEndStringInFile(libPathsMap.get(libName.toLowerCase()), string);
    }
    
    public static String getRanJoin() {
        return getRandomStringFromLib("join");
    }
    
    public static String getRanLeave() {
        return getRandomStringFromLib("leave");
    }
    
    public static String getRanSentence() {
        return getRandomStringFromLib("misc");
    }
    
     public static boolean addRanJoin(String string) {
         return addStringToLib("join", string);
     }
    
     public static boolean addRanLeave(String string) {
         return addStringToLib("leave", string);
     }
    
     public static boolean addRanSentence(String string) {
         return addStringToLib("misc", string);
     }
     
    public static char getRandomChar() {
        Random ran = new Random();
        return (char) (65 + ran.nextInt(123));
    }
    
    public static String getRandomString(int par) {
        String s = "";
        for (int i = 0; i < par; i++) {
            s += getRandomChar();
        }
        return s;
    }
    
    public static TwitchColor getRandomColor() {
        TwitchColor[] colors = TwitchColor.values();
        try {
            return colors[new Random().nextInt(colors.length)];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return Main.defColor;
        }
    }
    
    public static String getTimeoutMsg(int timeouttime) {
        return null;
    }
}
