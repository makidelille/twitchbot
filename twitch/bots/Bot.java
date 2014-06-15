
package twitch.bots;

import java.util.ArrayList;
import java.util.HashMap;

import org.jibble.pircbot.PircBot;

import twitch.Main;
import twitch.util.RandomText;
import twitch.util.Script;
import twitch.util.StreamerData;
import twitch.util.TwitchColor;


public class Bot extends PircBot {
    
    private static int        spamDelay    = 60;
    public boolean            isPaused     = false;
    public boolean            forceQuit    = false;
    private boolean           debugMode    = false;
    private boolean           silentMode   = false;
    private boolean           antiSpam     = false;
    private boolean           alwaysAnswer = true;
    private boolean           hello        = true;
    private long              lastCmdtime;
    private int defTimeouttime = 300;
    private RBot              rBot;
    private StreamerData      stream;
    private ArrayList<String> userWithMaster;
    private ArrayList<String> usersHello;
    
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
        // hello script
        if (usersHello.contains(sender.toLowerCase())) {
            sendText(channel, RandomText.getRanJoin(), sender);
            usersHello.remove(sender.toLowerCase());
        }
        // check the scripts
        if (!isPaused) {
            for (Script script : stream.getScripts()) {
                if (script.execute(this, channel, sender, msg)) return;
            }
            for (Script script : StreamerData.common.getScripts()) {
                if (script.execute(this, channel, sender, msg)) return;
            }
        }
        // check the bot antispam
        if (!stream.isUserOp(sender) && (difS < spamDelay && antiSpam)) return;
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0].toLowerCase();
        boolean singleCmd = msgArray.length == 1;
        // check pause des cmds
        if ((isPaused && !stream.isUserOp(sender)) || stream == null) return;
        // bot cmds
        if (stream.isUserOp(sender)) {
            switch (cmd) {
                case "!ping":
                    sendMeText(channel, "pong", sender);
                    return;
                case "!leave":
                    if (userWithMaster.contains(sender.toLowerCase())) {
                        sendMeText(channel, "Bot leaving and returning home ...", sender);
                        this.partChannel(channel);
                        this.joinChannel(Main.MASTERCHANNEL);
                    }
                    return;
                case "!join":
                    if (!singleCmd && (userWithMaster.contains(sender.toLowerCase()))) {
                        sendMeText(channel, "Joining " + msgArray[1], sender);
                        try {
                            silentMode = !Boolean.valueOf(msgArray[2]);
                        } catch (IndexOutOfBoundsException e) {
                        }
                        this.partChannel(getStreamChannel());
                        this.joinChannel("#" + msgArray[1]);
                    }
                    return;
                case "!quit":
                    if (userWithMaster.contains(sender.toLowerCase())) {
                        quit();
                    }
                    return;
                case "!pause":
                    if (isPaused) sendMeText(channel, "[@s] --> FailFish", sender, TwitchColor.ORANGE);
                    else sendMeText(channel, "Bot en pause", sender,TwitchColor.RED);
                    this.isPaused = true;
                    return;
                case "!resume":
                    if (isPaused) sendMeText(channel, "Bot actif", sender,TwitchColor.GREEN);
                    else sendMeText(channel, "Bot deja actif",sender, TwitchColor.GREEN);
                    this.isPaused = false;
                    return;
                case "!reload":
                    sendText(channel, "Reload", sender);
                    Main.load();
                    stream = StreamerData.getStreamerData(channel);
                    return;
                case "!logbot":
                    try {
                        if (msgArray[1].equalsIgnoreCase("on")) {
                            sendMeText(channel, "log bot activé", sender);
                            this.rBot = new RBot(this);
                            this.rBot.joinChannel(getStreamChannel());
                        } else if (msgArray[1].equalsIgnoreCase("off")) {
                            sendMeText(channel, "log bot d�sactivé", sender);
                            if (this.rBot != null) this.rBot.dispose();
                            this.rBot = null;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        sendText(channel, "par manquant", sender);
                    }
                    return;
                case "!debug":
                    if (userWithMaster.contains(sender.toLowerCase())) {
                        debugMode = !debugMode;
                        sendMeText(channel, "Debug Mode : " + (debugMode ? "On" : "Off"), sender, TwitchColor.RED);
                        this.setVerbose(debugMode);
                    }
                    return;
                case "!alwaysanswer":
                    if (alwaysAnswer) {
                        sendMeText(channel, "je reponds deja à tout :D", sender, TwitchColor.DARKPINK);
                        return;
                    }
                    this.alwaysAnswer = true;
                    sendMeText(channel, "je repondrai toujours <3", sender, TwitchColor.DARKPINK);
                    return;
                case "!neveranswer":
                    if (!alwaysAnswer) {
                        sendMeText(channel, "je reponds deja qu'aux bons Kappa", sender, TwitchColor.CHOCOLATE);
                        return;
                    }
                    this.alwaysAnswer = false;
                    sendMeText(channel, "je repondrai qu'aux bons :P", sender, TwitchColor.CHOCOLATE);
                    return;
                case "!hello":
                    hello = !hello;
                    sendMeText(channel, "hello Mode : " + (hello ? "On" : "Off"), sender, TwitchColor.BLUE);
                    return;
                case "!botspam":
                    if (singleCmd) {
                        antiSpam = true;
                        sendMeText(channel, "AntiSpam actif avec pour délai : " + spamDelay + "s", sender, TwitchColor.RED);
                        return;
                    }
                    try {
                        if (msgArray[1].equalsIgnoreCase("off")) {
                            antiSpam = false;
                            sendMeText(channel, "AntiSpam éteint.Attention le chaos arrive...", sender, TwitchColor.GREEN);
                            return;
                        } else if (msgArray[1].equalsIgnoreCase("set")) {
                            spamDelay = Integer.valueOf(msgArray[2]);
                            sendMeText(channel, "Temps entre deux message : " + spamDelay + "s", sender);
                            return;
                        } else if (msgArray[1].equalsIgnoreCase("on")) {
                            try {
                                spamDelay = Integer.valueOf(msgArray[2]);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            } finally {
                                antiSpam = true;
                                sendMeText(channel, "AntiSpam actif avec pour délai : " + spamDelay + "s", sender, TwitchColor.RED);
                            }
                            return;
                        }
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        sendText(channel, "[@s] : erreur dans les arguments, y a pas de débat possible :o", sender);
                    }
                    return;
                case "!addmaster":
                    if (userWithMaster.contains(sender.toLowerCase())) {
                        try {
                            userWithMaster.add(msgArray[1].toLowerCase());
                            sendText(channel, "[@s] est maintenant un master", msgArray[1]);
                        } catch (IndexOutOfBoundsException e) {}
                    }
                    return;
            }
        }
        // channels cmds
        if (!handleChannelMsg(channel, sender, msg)) {
            if (!handleChannelMsg(StreamerData.common.getName(), sender, msg)) {
                if (msg.startsWith("!") && alwaysAnswer) sendText(channel, "hum...NOPE", sender);
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
    }
    
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            Main.log("RW BOT READY");
            if (rBot != null) rBot.joinChannel(channel);
            if (!silentMode) sendText(channel, RandomText.getRanJoin(), "");
            stream = StreamerData.getStreamerData(channel);
            userWithMaster = new ArrayList<String>();
            usersHello = new ArrayList<String>();
            userWithMaster.add("makidelille");
            userWithMaster.add(stream.getName().toLowerCase());
        }
        if (!usersHello.contains(sender.toLowerCase()) && hello) usersHello.add(sender.toLowerCase());
    }
    
    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        super.onPart(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            if(rBot != null) rBot.partChannel(channel);
            if (!silentMode) sendText(channel, RandomText.getRanLeave(), "");
            stream = null;
        }
        if (usersHello.contains(sender.toLowerCase())) usersHello.remove(sender.toLowerCase());
    }
    
    @Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);
        if (mode.contains("+o")) {
            try {
                String modo = mode.split(" ")[2];
                if (!stream.modo.contains(modo)) {
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
        this.sendMessage(channel, "[Bot] " + newMsg);
        if (!stream.isUserOp(sender)) this.lastCmdtime = System.nanoTime();
    }
    
//    public void sendMeText(String channel, String msg) {
//        this.sendMeText(channel, msg, "", Main.defColor);
//    }
//    
//    public void sendMeText(String channel, String msg, TwitchColor color) {
//        this.sendMeText(channel, msg, "", color);
//    }
    
    public void sendMeText(String channel, String msg, String sender, TwitchColor color) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        if (msg.startsWith("[@m]")) msg = msg.substring(4);
        String newMsg = msg.replace("[@s]", sender);
        this.sendMessage(channel, "/color " + color.getColorCode());
        this.sendMessage(channel, "/me [Bot] " + newMsg);
        this.sendMessage(channel, "/color " + Main.defColor.getColorCode());
        if (!stream.isUserOp(sender)) this.lastCmdtime = System.nanoTime();
    }
    
    public void sendMeText(String channel, String msg, String sender) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        this.sendMeText(channel, msg, sender, Main.defColor);
    }
    
    public void sendRainbow(String channel, TwitchColor start) {
        TwitchColor curentcolor = start;
        this.setMessageDelay(100);
        do {
            sendMessage(channel, "/color " + curentcolor.getColorCode());
            sendMessage(channel, "/me Color : " + curentcolor.getColorCode());
            curentcolor = TwitchColor.getNextColor(curentcolor);
        } while (curentcolor != start);
        this.setMessageDelay(100);
        sendMessage(channel, "/color " + Main.defColor);
    }
    
    public boolean handleChannelMsg(String channel, String sender, String msg) {
        StreamerData tempStream = channel.equals(StreamerData.common.getName()) ? StreamerData.common : stream;
        boolean op = tempStream.isUserOp(sender);
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
                        sendText(getStreamChannel(), "commande ajoutée", sender);
                        stream = StreamerData.getStreamerData(channel);
                    } else sendText(getStreamChannel(), "Erreur dans l'ajout de la commande", sender);
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
                        sendText(getStreamChannel(), "commande supprimée", sender);
                        stream = StreamerData.getStreamerData(channel);
                    } else sendText(getStreamChannel(), "Erreur dans la suppression de  la commande", sender);
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
                        sendText(getStreamChannel(), "commande ajoutée", sender);
                        StreamerData.common.init();
                    } else sendText(getStreamChannel(), "Erreur dans l'ajout de la commande", sender);
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
                        sendText(getStreamChannel(), "commande supprimée", sender);
                        StreamerData.common.init();
                    } else sendText(getStreamChannel(), "Erreur dans la suppression de  la commande", sender);
                }
                return true;
            }
        }
        return false;
    }
    
    public String getStreamChannel() {
        return "#" + stream.getName();
    }
    
    private void quit() {
        this.sendMeText(getStreamChannel(), "BOT STOPPING", "");
        Main.log("BOTS DISCONNECTING");
        rBot.disconnect();
        this.disconnect();
        rBot.dispose();
        this.dispose();
        Main.stop();
    }
    
    public StreamerData getStream() {
        return this.stream;
    }

    public void timeout(String sender){
        this.timeout(sender,defTimeouttime);
    }
    
    public void timeout(String sender,int time) {
        sendMessage(getStreamChannel(), "/timeout " + sender+" " + time);
        Main.log("timeout de "+sender+" pour "+time +"s");
    }
}
