
package twitch.lib;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitch.Main;
import twitch.bots.Bot;
import twitch.lib.data.ComData;
import twitch.lib.data.MakidelilleData;
import twitch.lib.data.Monstro99Data;
import twitch.util.FileRWHelper;


public abstract class StreamerData {
    
    public static HashMap<String, StreamerData> map;
    protected String                            channel;
    private Path                                cmdsFile;
    private HashMap<String, String>             cmdsBasics;
    private ArrayList<String>                   cmdsSpecial;
    public ArrayList<String>                    modo;
    
    public abstract void init();
    
    public abstract void onSpecialCmd(Bot bot, String sender, String msg);
    
    private static StreamerData monstro99;
    private static StreamerData makidelille;
    public static StreamerData  common;
    
    public static void load() {
        map = new HashMap<String, StreamerData>();
        monstro99 = new Monstro99Data();
        makidelille = new MakidelilleData();
        common = new ComData();
        monstro99.init();
        makidelille.init();
        common.init();
    }
    
    public static StreamerData getStreamerData(String channel) {
        if (channel.startsWith("#")) channel = channel.substring(1);
        return map.containsKey(channel) ? map.get(channel) : null;
    }
    
    public boolean isUserOp(String channel, String userToCompare) {
        if (userToCompare.equalsIgnoreCase(Main.MASTER) || userToCompare.equalsIgnoreCase("makidelille") || userToCompare.equalsIgnoreCase(channel.substring(1))) return true;
        for (String user : modo) {
            if (user.equals(userToCompare)) return true;
        }
        return false;
    }
    
    public StreamerData(String channel) {
        map.put(channel, this);
        this.modo = new ArrayList<String>();
        this.channel = channel;
        try {
            cmdsFile = Paths.get(Main.APPDATA + channel + ".txt");
            Main.log("chargement de : " + cmdsFile.toAbsolutePath().toString());
            if (!Files.exists(cmdsFile)) {
                FileRWHelper.createNewFile(cmdsFile);
                FileRWHelper.writeEndStringInFile(cmdsFile, "#" + this.channel);
            }
            generateCmdsMap();
        } catch (NullPointerException e) {
            e.printStackTrace();
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
            if (display.startsWith("[@m]")) {
                bot.sendMeText(bot.getStreamChannel(), display, sender);
            } else {
                bot.sendText(bot.getStreamChannel(), display, sender);
            }
            return;
        }
    }
    
    public boolean isChannelCmd(String cmd) {
        return this.getCmds().containsKey(cmd.toLowerCase()) || this.cmdsSpecial.contains(cmd.toLowerCase());
    }
    
    public boolean isChannelSpecialCmd(String cmd) {
        return this.cmdsSpecial.contains(cmd.toLowerCase());
    }
    
    private void generateCmdsMap() {
        cmdsSpecial = new ArrayList<String>();
        cmdsBasics = new HashMap<String, String>();
        try {
            List<String> lines = Files.readAllLines(cmdsFile, Charset.defaultCharset());
            for (String line : lines) {
                if (line.startsWith("#")) {
                    continue;
                } else if (line.startsWith("!")) {
                    String[] array = line.split("=");
                    try {
                        if (!cmdsBasics.containsKey(array[0])) {
                            Main.log("Commande : " + array[0].toLowerCase() + " | Réponse : " + array[1]);
                            cmdsBasics.put(array[0].toLowerCase(), array[1]);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                } else if (line.startsWith("@")) {
                    line = line.replace('@', '!');
                    Main.log("Commande speciale : " + line);
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
        return FileRWHelper.writeEndStringInFile(cmdsFile, s);
    }
    
    public boolean removeCommand(String cmd) {
        if (!cmdsBasics.containsKey("!" + cmd.toLowerCase())) return false;
        String s = "!" + cmd + "=" + cmdsBasics.get("!" + cmd);
        return FileRWHelper.deleteString(cmdsFile, s, "#" + this.channel);
    }
    
    public String getCommandsList() {
        String re = "";
        for (String line : cmdsBasics.keySet())
            re += line + " | ";
        for (String line : cmdsSpecial)
            re += line + " | ";
        return re;
    }
    
    public String getName() {
        return this.channel;
    }
}
