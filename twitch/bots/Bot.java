
package twitch.bots;

import java.sql.Time;
import java.util.HashMap;

import org.jibble.pircbot.PircBot;

import twitch.Main;
import twitch.lib.RandomText;
import twitch.lib.StreamerData;


public class Bot extends PircBot {
    
    public boolean       isPaused;
    private boolean      debugMode    = false;
    private boolean      silentMode   = false;
    public boolean       forceQuit    = false;
    private StreamerData stream       = null;
    private RBot         rBot;
    private boolean      alwaysAnswer = true;
    
    public Bot(String name, boolean silentMode) {
        this.setName(name);
        this.setMessageDelay(10);
        this.isPaused = false;
        new HashMap<String, Boolean>();
        this.silentMode = silentMode;
    }
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String msg) {
        if ((isPaused && stream.isUserOp(getStreamChannel(), sender)) || stream == null) return;
        if (msg.toLowerCase().contains("hodor") && !msg.startsWith("!") && !isPaused) {
            sendMessage(channel, "/me HODOR");
            return;
        }
        if (msg.contains("Xd") && !sender.equalsIgnoreCase("makidelille")) {
            sendText(channel, "Toi aussi, tu sais pas les faire Xd");
            return;
        }
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0].toLowerCase();
        boolean singleCmd = msgArray.length == 1;
        if (stream.isUserOp(channel, sender)) {
            switch (cmd) {
                case "!ping":
                    sendMeText(channel, "pong");
                    return;
                case "!delay":
                    if (singleCmd) {
                        sendMeText(channel, "latence entre deux messages " + this.getMessageDelay() + "ms");
                    } else {
                        int time = Integer.valueOf(msgArray[1]);
                        this.setMessageDelay(time);
                        sendMeText(channel, "latence entre deux messages " + time + "ms");
                    }
                    return;
                case "!leave":
                    sendMeText(channel, "Bot leaving and returning home ...");
                    this.partChannel(channel);
                    this.joinChannel(Main.MASTERCHANNEL);
                    return;
                case "!join":
                    if (!singleCmd && (sender.equalsIgnoreCase(Main.MASTER) || sender.equalsIgnoreCase("makidelille"))) {
                        sendMeText(channel, "Joining " + msgArray[1]);
                        this.partChannel(getStreamChannel());
                        this.joinChannel("#" + msgArray[1]);
                    }
                    return;
                case "!quit":
                    if (sender.equalsIgnoreCase(Main.MASTER) || sender.equalsIgnoreCase("makidelille")) {
                        this.quitServer("ended by " + sender);
                        Main.log("STOPPING...");
                        this.forceQuit = true;
                    }
                    return;
                case "!pause":
                    sendMeText(channel, "Bot en pause");
                    this.isPaused = true;
                    return;
                case "!resume":
                    if (isPaused) sendMeText(channel, "Bot actif");
                    else sendMeText(channel, "Bot deja actif");
                    this.isPaused = false;
                    return;
                case "!reload":
                    sendText(channel, "Reload");
                    Main.load();
                    return;
                case "!debug":
                    if (sender.equalsIgnoreCase("makidelille")) {
                        debugMode = !debugMode;
                        sendMeText(channel, "Debug Mode : " + (debugMode ? "On" : "Off"));
                        this.setVerbose(debugMode);
                    }
                    return;
                case "!alwaysanswer":
                    if (alwaysAnswer) {
                        sendMeText(channel, "je reponds deja Ã  tout :D");
                        return;
                    }
                    this.alwaysAnswer = true;
                    sendMeText(channel, "je repondrai toujours <3");
                    return;
                case "!neveranswer":
                    if (!alwaysAnswer) {
                        sendMeText(channel, "je reponds deja qu'aux bons Kappa");
                        return;
                    }
                    this.alwaysAnswer = false;
                    sendMeText(channel, "je repondrai qu'aux bons :P");
                    return;
            }
        }
        if (!handleChannelMsg(channel, sender, msg)) if (!handleChannelMsg(StreamerData.common.getName(), sender, msg)) {
            if (msg.startsWith("!") && alwaysAnswer) sendText(channel, "hum...NOPE");
        } else {
            return;
        }
    }
    
    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        if (this.forceQuit) {
            this.dispose();
            Time dif = new Time(System.currentTimeMillis() - Main.startTime);
            Main.log("EXECUTION TIME : " + dif);
            Main.log("END");
            System.out.close();
        }
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
            Main.log("BOT READY");
            rBot.joinChannel(channel);
            if (!silentMode) sendText(channel, RandomText.getRanJoin());
            stream = StreamerData.getStreamerData(channel);
        }
    }
    
    @Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);
        if (mode.contains("+o")) {
            try {
                String modo = mode.split(" ")[2];
                Main.log(modo + " a été ajouter à la liste des modos du stream de " + stream.getName());
                stream.modo.add(modo);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            }
        }
    }
    
    public void sendText(String channel, String msg, String sender) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        String newMsg = msg.replace("@s", sender);
        this.sendText(channel, newMsg);
    }
    
    public void sendText(String channel, String msg) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        this.sendMessage(channel, "[Bot] " + msg);
    }
    
    public void sendMeText(String channel, String msg, String sender) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        if (msg.startsWith("@m")) msg = msg.substring(2);
        String newMsg = msg.replace("@s", sender);
        this.sendMeText(channel, newMsg);
    }
    
    public void sendMeText(String channel, String msg) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        this.sendMessage(channel, "/me [Bot] " + msg);
    }
    
    public boolean handleChannelMsg(String channel, String sender, String msg) {
        StreamerData tempStream = StreamerData.getStreamerData(channel);
        boolean success = false;
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0];
        if (tempStream != null) {
            if (tempStream.isChannelCmd(cmd)) {
                tempStream.onCmd(this, sender, msg);
                return true;
            } else if (cmd.equalsIgnoreCase("!addcmd")) {
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
                        sendText(getStreamChannel(), "commande ajoutÃ©e");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans l'ajout de la commande");
                }
                return true;
            } else if (cmd.equalsIgnoreCase("!delcmd")) {
                try {
                    success = tempStream.removeCommand(msgArray[1]);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) {
                        sendText(getStreamChannel(), "commande supprimÃ©e");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans la suppression de  la commande");
                }
                return true;
            } else if (cmd.equalsIgnoreCase("!addgcmd") && tempStream == StreamerData.common && sender.equalsIgnoreCase(Main.MASTER)) {
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
                        sendText(getStreamChannel(), "commande ajoutÃ©e");
                        Main.load();
                    } else sendText(getStreamChannel(), "Erreur dans l'ajout de la commande");
                }
                return true;
            } else if (cmd.equalsIgnoreCase("!delgcmd") && tempStream == StreamerData.common && sender.equalsIgnoreCase(Main.MASTER)) {
                try {
                    success = tempStream.removeCommand(msgArray[1]);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) {
                        sendText(getStreamChannel(), "commande supprimÃ©e");
                        Main.load();
                    }
                    else sendText(getStreamChannel(), "Erreur dans la suppression de  la commande");
                }
                return true;
            }
        }
        return false;
    }
    
    public String getStreamChannel() {
        return stream.getName();
    }
}
