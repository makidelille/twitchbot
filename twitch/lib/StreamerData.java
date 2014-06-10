
package twitch.lib;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import twitch.Bot;
import twitch.Main;


public abstract class StreamerData {
    
    public static HashMap<String, StreamerData> map;
    protected String                            channel;
    private Path                                cmdsFile;
    private HashMap<String, String>             cmdsBasics;
    private ArrayList<String>                   cmdsSpecial;
    
    public abstract void init();
    
    public abstract void onSpecialCmd(Bot bot, String sender, String msg);
    
    private static StreamerData monstro99;
    private static StreamerData makidelille;
    
    public static void load() {
        map = new HashMap<String, StreamerData>();
        monstro99 = new Monstro99();
        makidelille = new Makidelille();
        monstro99.init();
        makidelille.init();
    }
    
    public StreamerData(String channel) {
        map.put(channel, this);
        this.channel = "#" + channel;
        cmdsFile = Paths.get(channel + ".txt");
        if (!Files.exists(cmdsFile)) createCmdFile();
        generateCmdsMap();
    }
    
    private boolean createCmdFile() {
        Main.log("Creating new file for channel : " + channel);
        try {
            File file = new File(channel.substring(1) + ".txt");
            file.createNewFile();
            Main.log("File Created succesfully");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public HashMap<String, String> getCmds() {
        return cmdsBasics;
    }
    
    public ArrayList<String> getSpecialCmds() {
        return cmdsSpecial;
    }
    
    public void onCmd(Bot bot, String sender, String msg) {
        String cmd = msg.split(" ")[0];
        if (this.isChannelSpecialCmd(cmd)) {
            onSpecialCmd(bot, sender, msg);
            return;
        } else {
            String display = cmdsBasics.get(cmd);
            bot.sendText(this.channel, display, sender);
            return;
        }
    }
    
    public boolean isChannelCmd(String cmd) {
        return this.getCmds().containsKey(cmd.toLowerCase())
                || this.cmdsSpecial.contains(cmd.toLowerCase());
    }
    
    public boolean isChannelSpecialCmd(String cmd) {
        return this.cmdsSpecial.contains(cmd.toLowerCase());
    }
    
    public static String getChannelWithCmd(String cmd) {
        Iterator<Entry<String, StreamerData>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, StreamerData> entry = it.next();
            if (entry.getValue().isChannelCmd(cmd)) return entry.getKey();
        }
        return null;
    }
    
    private void generateCmdsMap() {
        cmdsSpecial = new ArrayList<String>();
        cmdsBasics = new HashMap<String, String>();
        try {
            List<String> lines = Files.readAllLines(cmdsFile,
                    StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.startsWith("_") || line.startsWith("//")) {
                    continue;
                } else if (line.startsWith("!")) {
                    String[] array = line.split("=");
                    try {
                        if (!cmdsBasics.containsKey(array[0]))
                            cmdsBasics.put(array[0].toLowerCase(), array[1]);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                } else if (line.startsWith("@")) {
                    line = line.replace('@', '!');
                    cmdsSpecial.add(line.toLowerCase());
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addCommand(String cmdName, String display) {
        if (cmdsBasics.containsKey("!" + cmdName.toLowerCase())) return false;
        String s = "!" + cmdName.toLowerCase() + "=" + display;
        try {
            Files.write(cmdsFile, s.getBytes(StandardCharsets.UTF_8));
            this.generateCmdsMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removeCommand(String cmd) {
        if (!cmdsBasics.containsKey("!" + cmd.toLowerCase())) return false;
        try {
            List<String> temp = Files.readAllLines(cmdsFile,
                    StandardCharsets.UTF_8);
            for (String line : temp) {
                try {
                    if (line.split("=")[0].equalsIgnoreCase("!" + cmd)) {
                        temp.remove(line);
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            try {
                Files.deleteIfExists(cmdsFile);
                if (!createCmdFile()) return false;
                for (String tempS : temp) {
                    try {
                        Files.write(cmdsFile,
                                tempS.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            this.generateCmdsMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
