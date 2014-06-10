
package twitch;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jibble.pircbot.PircBot;

import twitch.lib.StreamerData;


public class Bot extends PircBot {
    
    public boolean                   isPaused;
    private String                   selected;
    private boolean                  votebanInprogress;
    private HashMap<String, Boolean> votes;
    private ArrayList<String>        modo;
    private boolean                  debugMode  = false;
    private boolean                  silentMode = false;
    public boolean                   forceQuit  = false;
    
    public Bot(String name, boolean silentMode) {
        this.setName(name);
        this.setMessageDelay(10);
        this.isPaused = false;
        this.selected = "";
        this.votes = new HashMap<String, Boolean>();
        this.modo = new ArrayList<String>();
        this.silentMode = silentMode;
    }
    
    // TODO implements cmd level
    // TODO add !addcmd and !delcmd
    @Override
    protected void onMessage(String channel, String sender, String login,
            String hostname, String msg) {
        if (isPaused && !sender.equalsIgnoreCase(Main.MASTER)) return;
        // line to interrupt any cmd if bot is in debug mode
        if (debugMode && !sender.equalsIgnoreCase(Main.MASTER)
                && msg.startsWith("!")) {
            sendText(channel, "hum... NOPE");
            return;
        }
        super.onMessage(channel, sender, login, hostname, msg);
        if (msg.toLowerCase().contains("hodor")) {
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
        switch (cmd) {
            case "!ping":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    sendText(channel, "pong");
                }
                return;
            case "!delay":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    if (singleCmd) {
                        sendText(channel,
                                "Refresh Rate set to " + this.getMessageDelay()
                                        + "ms");
                    } else {
                        int time = Integer.valueOf(msgArray[1]);
                        this.setMessageDelay(time);
                        sendText(channel, "Refresh Rate set to " + time + "ms");
                    }
                }
                return;
            case "!leave":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    sendText(channel, "Bot leaving and returning home ...");
                    this.partChannel(channel);
                    this.joinChannel("#makidelille");
                }
                return;
            case "!quit":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    this.quitServer("ended by " + sender);
                    Main.log("STOPPING...");
                    this.forceQuit = true;
                }
                return;
            case "!debug":
                if (sender.equalsIgnoreCase("makidelille")) {
                    debugMode = !debugMode;
                    sendText(channel, "Debug Mode : "
                            + (debugMode ? "On" : "Off"));
                }
                return;
            case "!pause":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    sendText(channel, "Bot is paused ...");
                    this.isPaused = true;
                }
                return;
            case "!join":
                if ((sender.equalsIgnoreCase(Main.MASTER) || sender
                        .equalsIgnoreCase("makidelille")) && !singleCmd) {
                    sendText(channel, "Joining " + msgArray[1]);
                    this.partChannel(channel);
                    this.joinChannel("#" + msgArray[1]);
                }
                return;
            case "!resume":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    if (isPaused) sendText(channel, "Bot is working ...");
                    else sendText(channel, "Bot is already working ...");
                    this.isPaused = false;
                }
                return;
            case "!reload":
                if (sender.equalsIgnoreCase(Main.MASTER)
                        || sender.equalsIgnoreCase("makidelille")) {
                    sendText(channel, "Reloading...");
                    StreamerData.load();
                }
                return;
            case "!time":
                Date time = new Date();
                String date = DateFormat.getInstance().format(time);
                sendText(channel, "Local time : " + date);
                return;
            case "!lien":
                if (singleCmd) {
                    handleChannelMsg(channel, sender, "!lien");
                    return;
                }
                String querry = msg.substring(cmd.length() + 1).replace(" ",
                        "+");
                sendText(channel, "lmgtfy.com/?q=" + querry);
                return;
            case "!votetimeout":
                if (singleCmd) return;
                selected = msgArray[1];
                sendText(channel, sender + " veux timeout " + selected);
                sendText(channel, "!oui ou !non ?");
                votebanInprogress = true;
                votes = new HashMap<String, Boolean>();
                return;
            case "!oui":
                if (!votebanInprogress) {
                    sendText(channel, "Pas de !votetimeout en cours :D");
                    return;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), true);
                    sendText(channel, sender + ", Vote enregistrer");
                } else {
                    sendText(channel, sender
                            + ", T'as deja voter triche pas ^^");
                }
                return;
            case "!non":
                if (!votebanInprogress) {
                    sendText(channel, "Pas de !votetimeout en cours :D");
                    return;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), false);
                    sendText(channel, sender + ", Vote enregistrer");
                } else {
                    sendText(channel, sender
                            + ", T'as deja voter triche pas ^^");
                }
                return;
            case "!result":
                if (!votebanInprogress) return;
                sendText(channel, "Calcul en cours...");
                Iterator<Entry<String, Boolean>> it = votes.entrySet()
                        .iterator();
                int oui = 0,
                non = 0;
                while (it.hasNext()) {
                    Entry<String, Boolean> entry = it.next();
                    if (entry.getValue()) oui++;
                    else non++;
                }
                sendText(channel, "Le chat a voter "
                        + (oui > non ? "OUI a " + 100 * (oui / (oui + non))
                                + "%" : "NON a " + 100 * (non / (oui + non))
                                + "%") + " pour le cas de " + selected);
                // sendMessage(target, message); //TODO timeout
                votebanInprogress = false;
                return;
        }
        handleChannelMsg(channel, sender, msg);
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
    protected void onJoin(String channel, String sender, String login,
            String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            Main.log("BOT READY");
            if (!silentMode) sendText(channel, "Hello les gens :D ?");
        }
    }
    
    @Override
    protected void onUserMode(String targetNick, String sourceNick,
            String sourceLogin, String sourceHostname, String mode) {
        super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname,
                mode);
        System.out.println(targetNick);
        System.out.println(sourceNick);
        System.out.println(sourceLogin);
        System.out.println(sourceHostname);
        System.out.println(mode); // TODO work here
    }
    
    private boolean isUserOp(String channel, String userToCompare) {
        for (String user : modo) {
            if (user.equals(userToCompare)) return true;
        }
        return false;
    }
    
    public void sendText(String channel, String msg, String sender) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        String newMsg = msg.replace("@s", sender);
        this.sendText(channel, newMsg);
    }
    
    private void sendText(String channel, String msg) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        this.sendMessage(channel, "[Bot] " + msg);
    }
    
    public void handleChannelMsg(String channel, String sender, String msg) {
        boolean success = false;
        String ch = channel.substring(1);
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0];
        if (StreamerData.map.containsKey(ch)) {
            if (StreamerData.map.get(ch).isChannelCmd(cmd)) {
                StreamerData.map.get(ch).onCmd(this, sender, msg);
                return;
            } else if (cmd.equalsIgnoreCase("!addcmd")) {
                System.out.println("YOLO");
                try {
                    String dis = "";
                    for (int i = 2; i < msgArray.length; i++) {
                        dis += msgArray[i] + " ";
                    }
                    success = StreamerData.map.get(ch).addCommand(msgArray[1],
                            dis);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) sendText(channel,
                            "Commande ajouter avec success !");
                    else sendText(channel, "Erreur dans l'ajout de la commande");
                }
                return;
            } else if (cmd.equalsIgnoreCase("!delcmd")) {
                try {
                    success = StreamerData.map.get(ch).removeCommand(
                            msgArray[1]);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    if (success) sendText(channel,
                            "Commande supprimer avec success !");
                    else sendText(channel,
                            "Erreur dans la suppression de  la commande");
                }
                return;
            }
        }
    }
}
