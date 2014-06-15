
package twitch.data.streamData;

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
import twitch.data.userData.UserRights;
import twitch.data.userData.UserRights.AccessRight;
import twitch.files.FileRWHelper;
import twitch.scripts.Script;
import twitch.util.TwitchColor;


public abstract class StreamerData {
    
    public static HashMap<String, StreamerData> map;
    protected String channel;
    protected Path cmdsFile;
    protected HashMap<String, String> cmdsBasics;
    protected ArrayList<String> cmdsSpecial;
    protected ArrayList<Script> channelScripts;
    private UserRights users;
    
    public void init() {
        generateCmdsMap();
        generateSubCmds();
    }
    
    protected abstract void generateSubCmds();
    
    public abstract void onSpecialCmd(Bot bot, String sender, String msg);
    
    public static StreamerData common;
    
    public static void load() {
        // reset the streamer map
        map = new HashMap<String, StreamerData>();
        // instance the data
        new Monstro99Data();
        new MakidelilleData();
        common = new ComData();
        // init the data
        common.init();
    }
    
    public static StreamerData getStreamerData(String channel) {
        if (channel.startsWith("#")) channel = channel.substring(1);
        StreamerData stream = map.get(channel.toLowerCase());
        if (stream == null) return null;
        stream.init();
        return stream;
    }
    
    public StreamerData(String channel) {
        map.put(channel.toLowerCase(), this);
        this.channelScripts = new ArrayList<Script>();
        this.channel = channel;
        users = new UserRights();
        getUsers().addStreamer(channel);
        try {
            cmdsFile = Paths.get(Main.APPDATA + channel + ".txt");
            Main.log("chargement de : " + cmdsFile.toAbsolutePath().toString());
            if (!Files.exists(cmdsFile)) {
                FileRWHelper.createNewFile(cmdsFile);
                FileRWHelper.writeEndStringInFile(cmdsFile, "#" + this.channel);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isUserOp(String userToCompare) {
        if (userToCompare.equalsIgnoreCase("makidelille")) return true;
        return AccessRight.isOp(getUsers().getAccessRight(userToCompare));
    }
    
    public boolean isUserMaster(String user) {
        if (user.equalsIgnoreCase("makidelille")) return true;
        return AccessRight.isMaster(getUsers().getAccessRight(user));

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
                try {
                    bot.sendMeText(bot.getStreamChannel(), display, sender, TwitchColor.getTwitchColor(msgArray[1]));
                } catch (IndexOutOfBoundsException e) {
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
    
    protected void generateCmdsMap() {
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
        if (!channelScripts.contains(script)) channelScripts.add(script);
    }
    
    public ArrayList<Script> getScripts() {
        return channelScripts;
    }

    public UserRights getUsers() {
        return users;
    }

}
