
package twitch.util;

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
import twitch.data.ComData;
import twitch.data.MakidelilleData;
import twitch.data.Monstro99Data;
import twitch.scripts.HodorScript;
import twitch.scripts.XdScript;


public abstract class StreamerData {
    
    public static HashMap<String, StreamerData> map;
    protected String                            channel;
    private Path                                cmdsFile;
    private HashMap<String, String>             cmdsBasics;
    private ArrayList<String>                   cmdsSpecial;
    public ArrayList<String>                    modo;
    private ArrayList<Script> channelScripts;
    
    
    public abstract void init();
    
    public abstract void onSpecialCmd(Bot bot, String sender, String msg);
    
    private static StreamerData monstro99;
    private static StreamerData makidelille;
    public static StreamerData  common;
    
    public static void load() {
        //reset the streamer map
        map = new HashMap<String, StreamerData>();
        
        //instance the data
        monstro99 = new Monstro99Data();
        makidelille = new MakidelilleData();
        common = new ComData();
        
        //init the data
        monstro99.init();
        makidelille.init();
        common.init();
        
        //start the scripts
        new HodorScript();
        new XdScript();
        
        
    }
    
    public static StreamerData getStreamerData(String channel) {
        if (channel.startsWith("#")) channel = channel.substring(1);
        return map.containsKey(channel.toLowerCase()) ? map.get(channel.toLowerCase()) : null;
    }
    
    public boolean isUserOp(String channel, String userToCompare) {
        if (userToCompare.equalsIgnoreCase(Main.MASTER) || userToCompare.equalsIgnoreCase("makidelille") || userToCompare.equalsIgnoreCase(channel.substring(1))) return true;
        for (String user : modo) {
            if (user.equals(userToCompare)) return true;
        }
        return false;
    }
    
    public StreamerData(String channel) {
        map.put(channel.toLowerCase(), this);
        this.modo = new ArrayList<String>();
        this.channelScripts = new ArrayList<Script>();
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
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0];
        if (this.isChannelSpecialCmd(cmd)) {
            onSpecialCmd(bot, sender, msg);
            return;
        } else {
            String display = cmdsBasics.get(cmd);
            if (display.startsWith("[@m]")) {
                try{
                    bot.sendMeText(bot.getStreamChannel(), display, sender, TwitchColor.getTwitchColor(msgArray[1]));
                }catch(IndexOutOfBoundsException e){
                    bot.sendMeText(bot.getStreamChannel(), display, sender);
                }
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

    public void addScript(Script script) {
        if(!channelScripts.contains(script))channelScripts.add(script);
    }
    
    public ArrayList<Script> getScripts(){
        return channelScripts;
    }
}
