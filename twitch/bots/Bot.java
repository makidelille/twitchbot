
package twitch.bots;

import java.util.HashMap;

import org.jibble.pircbot.PircBot;

import twitch.Main;
import twitch.util.RandomText;
import twitch.util.StreamerData;
import twitch.util.TwitchColor;


public class Bot extends PircBot {
    
    private static int   spamDelay    = 60;
    private boolean      debugMode    = false;
    private boolean      silentMode   = false;
    public boolean       isPaused     = false;
    public boolean       forceQuit    = false;
    private boolean      alwaysAnswer = true;
    private StreamerData stream       = null;
    private RBot         rBot;
    private long         lastCmdtime;
    private boolean      antiSpam     = false;
    
    public Bot(String name, boolean silentMode) {
        this.setName(name);
        this.setMessageDelay(10);
        this.isPaused = false;
        new HashMap<String, Boolean>();
        this.silentMode = silentMode;
        lastCmdtime = System.nanoTime();
    }
    
    @Override
    protected void onAction(String sender, String login, String hostname, String target, String action) {
        super.onAction(sender, login, hostname, target, action);
        this.onMessage(target, sender, login, hostname, action);
    }
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String msg) {
        long cmdtime = System.nanoTime();
        int difS = (int) ((cmdtime - lastCmdtime) / Math.pow(10, 9));
        if ((isPaused && !stream.isUserOp(channel, sender)) || stream == null) return;
        if (msg.toLowerCase().contains("hodor") && !msg.startsWith("!") && !isPaused) {
            sendMeText(channel, "HODOR" , RandomText.getRandomColor());
            return;
        }
        if (msg.contains("Xd") && !sender.equalsIgnoreCase("makidelille")) {
            sendText(channel, "Toi aussi, tu sais pas les faire Xd");
            return;
        }
        if (!stream.isUserOp(channel, sender) && (difS < spamDelay && antiSpam)) return;
        lastCmdtime = cmdtime;
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0].toLowerCase();
        boolean singleCmd = msgArray.length == 1;
        if (stream.isUserOp(channel, sender)) {
            switch (cmd) {
                case "!ping":
                    sendMeText(channel, "pong");
                    return;
                case "!leave":
                    if (singleCmd && (sender.equalsIgnoreCase(Main.MASTER) || sender.equalsIgnoreCase("makidelille"))) {
                        sendMeText(channel, "Bot leaving and returning home ...");
                        this.partChannel(channel);
                        this.joinChannel(Main.MASTERCHANNEL);
                    }
                    return;
                case "!join":
                    if (!singleCmd && (sender.equalsIgnoreCase(Main.MASTER) || sender.equalsIgnoreCase("makidelille"))) {
                        sendMeText(channel, "Joining " + msgArray[1]);
                        try{
                            silentMode = !Boolean.valueOf(msgArray[2]);
                        }catch(IndexOutOfBoundsException e){}
                        
                        this.partChannel(getStreamChannel());
                        this.joinChannel("#" + msgArray[1]);
                    }
                    return;
                case "!quit":
                    if (sender.equalsIgnoreCase(Main.MASTER) || sender.equalsIgnoreCase("makidelille")) {
                        quit();
                    }
                    return;
                case "!pause":
                    if (isPaused) sendMeText(channel, "[@s] --> FailFish", sender , TwitchColor.ORANGE);
                    else sendMeText(channel, "Bot en pause", TwitchColor.RED);
                    this.isPaused = true;
                    return;
                case "!resume":
                    if (isPaused) sendMeText(channel, "Bot actif", TwitchColor.GREEN);
                    else sendMeText(channel, "Bot deja actif", TwitchColor.GREEN);
                    this.isPaused = false;
                    return;
                case "!reload":
                    sendText(channel, "Reload");
                    Main.load();
                    return;
                case "!debug":
                    if (sender.equalsIgnoreCase(Main.MASTER)) {
                        debugMode = !debugMode;
                        sendMeText(channel, "Debug Mode : " + (debugMode ? "On" : "Off"), TwitchColor.RED);
                        this.setVerbose(debugMode);
                    }
                    return;
                case "!alwaysanswer":
                    if (alwaysAnswer) {
                        sendMeText(channel, "je reponds deja à tout :D", TwitchColor.DARKPINK);
                        return;
                    }
                    this.alwaysAnswer = true;
                    sendMeText(channel, "je repondrai toujours <3", TwitchColor.DARKPINK);
                    return;
                case "!neveranswer":
                    if (!alwaysAnswer) {
                        sendMeText(channel, "je reponds deja qu'aux bons Kappa", TwitchColor.CHOCOLATE);
                        return;
                    }
                    this.alwaysAnswer = false;
                    sendMeText(channel, "je repondrai qu'aux bons :P", TwitchColor.CHOCOLATE);
                    return;
                case "!spam":
                    if (singleCmd) {
                        antiSpam = true;
                        sendMeText(channel, "AntiSpam actif avec pour délai : " + spamDelay + "s" , TwitchColor.RED);
                        return;
                    }
                    try {
                        if (msgArray[1].equalsIgnoreCase("off")) {
                            antiSpam = false;
                            sendMeText(channel, "AntiSpam éteint.Attention le chaos arrive...", TwitchColor.GREEN);
                            return;
                        } else if (msgArray[1].equalsIgnoreCase("set")) {
                            spamDelay = Integer.valueOf(msgArray[2]);
                            sendMeText(channel, "Temps entre deux message : " + spamDelay + "s");
                            return;
                        } else if (msgArray[1].equalsIgnoreCase("on")) {
                            try {
                                spamDelay = Integer.valueOf(msgArray[2]);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            } finally {
                                antiSpam = true;
                                sendMeText(channel, "AntiSpam actif avec pour délai : " + spamDelay + "s", TwitchColor.RED);
                            }
                            return;
                        }
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        sendText(channel, "[@s] : erreur dans les arguments, y a pas de débat possible :o", sender);
                    }
                    return;
                case "!rainbow" :
                    try{
                        this.sendRainbow(channel, TwitchColor.getTwitchColor(msgArray[1]));
                    }catch(IndexOutOfBoundsException e){
                        this.sendRainbow(channel,Main.defColor);
                    }
                    return;
            }
        }
        if (!handleChannelMsg(channel, sender, msg)) {
            if (!handleChannelMsg(StreamerData.common.getName(), sender, msg)) {
                if (msg.startsWith("!") && alwaysAnswer) sendText(channel, "hum...NOPE");
            }
        } else {
            return;
        }
    }
    
    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        Main.stop();
    }
    
    @Override
    protected void onConnect() {
        super.onConnect();
        rBot = new RBot(this);
    }
    
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            Main.log("RW BOT READY");
            rBot.joinChannel(channel);
            if (!silentMode) sendText(channel, RandomText.getRanJoin(), "");
            stream = StreamerData.getStreamerData(channel);
        } else if (login.equalsIgnoreCase("FSG_SoWEeZ") || login.equalsIgnoreCase("schizolefrene") || login.equalsIgnoreCase("bubucho")) {
            sendText(channel, RandomText.getRanJoin(), login);
        }
    }
    
    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        super.onPart(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            Main.log("RW BOT READY");
            rBot.joinChannel(channel);
            if (!silentMode) sendText(channel, RandomText.getRanLeave(), "");
            stream = StreamerData.getStreamerData(channel);
        } else if (login.equalsIgnoreCase("FSG_SoWEeZ") || login.equalsIgnoreCase("schizolefrene") || login.equalsIgnoreCase("bubucho")) {
            sendText(channel, RandomText.getRanLeave(), login);
        }
    }
    
    @Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);
        if (mode.contains("+o")) {
            try {
                String modo = mode.split(" ")[2];
                if(!stream.modo.contains(modo)){
                    Main.log(modo + " a �t� ajouter � la liste des modos du stream de " + stream.getName());
                    stream.modo.add(modo);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            }
        }
    }
    
    public void sendText(String channel, String msg, String sender) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        String newMsg = msg.replace("[@s]", sender);
        this.sendText(channel, newMsg);
    }
    
    public void sendText(String channel, String msg) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        this.sendMessage(channel, "[Bot] " + msg);
    }
      
  
    
    public void sendMeText(String channel, String msg) {
        this.sendMeText(channel, msg, "", Main.defColor);
    }
    
    public void sendMeText(String channel,String msg, TwitchColor color){
        this.sendMeText(channel, msg, "", color);
    }
    
    
    public void sendMeText(String channel, String msg, String sender, TwitchColor color) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        if (msg.startsWith("[@m]")) msg = msg.substring(4);
        String newMsg = msg.replace("[@s]", sender);
        this.sendMessage(channel, "/color " + color.getColorCode());
        this.sendMessage(channel, "/me [Bot] " + newMsg);
        this.sendMessage(channel, "/color " + Main.defColor.getColorCode());
    }
    
    public void sendMeText(String channel, String msg ,String sender) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        this.sendMeText(channel, msg, sender, Main.defColor);
    }
    
    public void sendRainbow(String channel, TwitchColor start){
        TwitchColor curentcolor = start;
        this.setMessageDelay(1000);
        do{
            sendMessage(channel, "/color " + curentcolor.getColorCode());
            sendMessage(channel, "/me Color : " + curentcolor.getColorCode());
            curentcolor = TwitchColor.getNextColor(curentcolor);
        }while(curentcolor != start);
        this.setMessageDelay(10);
        sendMessage(channel, "/color "+ Main.defColor);
    }
    
    public boolean handleChannelMsg(String channel, String sender, String msg) {
        StreamerData tempStream = StreamerData.getStreamerData(channel);
        boolean op = tempStream.isUserOp(channel, sender);
        boolean success = false;
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0];
        if (tempStream != null) {
            if (tempStream.isChannelCmd(cmd)) {
                tempStream.onCmd(this, sender, msg);
                return true;
            } else if (cmd.equalsIgnoreCase("!addcmd") && op) {
                try {
                    String dis = "";
                    for (int i = 2; i < msgArray.length; i++) {
                        dis += msgArray[i] + " ";
                    }
                    success = tempStream.addCommand(msgArray[1], dis);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) {
                        sendText(getStreamChannel(), "commande ajoutée");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans l'ajout de la commande");
                }
                return true;
            } else if (cmd.equalsIgnoreCase("!delcmd") && op) {
                try {
                    success = tempStream.removeCommand(msgArray[1]);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) {
                        sendText(getStreamChannel(), "commande supprimée");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans la suppression de  la commande");
                }
                return true;
            } else if (cmd.equalsIgnoreCase("!addgencmd") && tempStream == StreamerData.common && sender.equalsIgnoreCase(Main.MASTER)) {
                try {
                    String dis = "";
                    for (int i = 2; i < msgArray.length; i++) {
                        dis += msgArray[i] + " ";
                    }
                    success = tempStream.addCommand(msgArray[1], dis);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) {
                        sendText(getStreamChannel(), "commande ajoutée");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans l'ajout de la commande");
                }
                return true;
            } else if (cmd.equalsIgnoreCase("!delgencmd") && tempStream == StreamerData.common && sender.equalsIgnoreCase(Main.MASTER)) {
                try {
                    success = tempStream.removeCommand(msgArray[1]);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) {
                        sendText(getStreamChannel(), "commande supprimée");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans la suppression de  la commande");
                }
                return true;
            }
        }
        return false;
    }
    
    public String getStreamChannel() {
        return stream.getName();
    }
    
    private void quit() {
        this.sendMeText(getStreamChannel(), "BOT STOPPING");
        Main.log("BOTS DISCONNECTING");
        rBot.disconnect();
        this.disconnect();
        rBot.dispose();
        this.dispose();
        Main.stop();
    }
}
