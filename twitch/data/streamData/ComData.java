
package twitch.data.streamData;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import twitch.Main;
import twitch.bots.Bot;
import twitch.util.RandomText;
import twitch.util.TwitchColor;


public class ComData extends StreamerData {
    
    private boolean                  votebanInprogress;
    private HashMap<String, Boolean> votes;
    private String                   selected;
    private Integer                  toTime;
    
    public ComData() {
        super("twitch");
    }
  
    
    @Override
    public void onSpecialCmd(Bot bot, String sender, String msg) {
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0].toLowerCase();
        boolean singleCmd = msgArray.length == 1;
        switch (cmd) {
            case "!time":
                Date time = new Date();
                String date = DateFormat.getInstance().format(time);
                bot.sendText(bot.getStreamChannel(), "Local time : " + date, sender);
                return;
            case "!lien":
                if (singleCmd) { return; }
                String querry = msg.substring(cmd.length() + 1).replace(" ", "+");
                bot.sendText(bot.getStreamChannel(), "lmgtfy.com/?q=" + querry, sender);
                return;
            case "!multi":
                String text = "multitwitch.tv/" + bot.getStream().getName() +"/";
                for(String arg : Arrays.copyOfRange(msgArray, 1, msgArray.length)){
                    text += arg +"/";
                }
                bot.sendText(bot.getStreamChannel(), text, sender);
                return;
            case "!random":
                if (singleCmd) {
                    bot.sendText(bot.getStreamChannel(), RandomText.getRanSentence(), sender);
                    return;
                }
                if (msgArray[1].equalsIgnoreCase("join")) {
                    try{
                        bot.sendText(bot.getStreamChannel(), sender +" : " + RandomText.getRanJoin(), msgArray[2]);
                    }catch(IndexOutOfBoundsException e){
                        bot.sendText(bot.getStreamChannel(), sender +" : essaie encore. Mais avec les bons parametres :P", sender);
                    }
                    return;
                }
                if (msgArray[1].equalsIgnoreCase("leave")) {
                    try{
                        bot.sendText(bot.getStreamChannel(), sender +" : " +RandomText.getRanLeave(), msgArray[2]);
                    }catch(IndexOutOfBoundsException e){
                        bot.sendText(bot.getStreamChannel(), sender +" : essaie encore. Mais avec les bons parametres :P", sender);
                    }
                    return;
                }
                try {
                    int value = Integer.valueOf(msgArray[1]);
                    Random rand = new Random();
                    bot.sendText(bot.getStreamChannel(), "" + rand.nextInt(value), sender);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    bot.sendText(bot.getStreamChannel(), "parametre non valide", sender);
                }
                return;
            case "!votetimeout":
                if (singleCmd || votebanInprogress) return;
                selected = msgArray[1];
                try {
                    toTime = Integer.valueOf(msgArray[2]);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    bot.sendText(bot.getStreamChannel(), "temps invalide, valeur par default = 300sec", sender);
                    toTime = 300;
                }
                bot.sendText(bot.getStreamChannel(), sender + " veux timeout " + selected, sender);
                bot.sendText(bot.getStreamChannel(), "!oui ou !non ?", sender);
                votebanInprogress = true;
                votes = new HashMap<String, Boolean>();
                return;
            case "!oui":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D", sender);
                    return;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), true);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistrÃ©", sender);
                } else {
                    bot.sendText(bot.getStreamChannel(), sender + ", T'as deja votÃ©, triche pas ^^", sender);
                }
                return;
            case "!yes":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D", sender);
                    return;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), true);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistrÃ©", sender);
                } else {
                    bot.sendText(bot.getStreamChannel(), sender + ", T'as deja votÃ©, triche pas ^^", sender);
                }
                return;
            case "!non":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D", sender);
                    return;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), false);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistrÃ©", sender);
                } else {
                    bot.sendText(bot.getStreamChannel(), sender + ", T'as deja votÃ©, triche pas ^^", sender);
                }
                return;
            case "!no":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D", sender);
                    return;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), false);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistrÃ©", sender);
                } else {
                    bot.sendText(bot.getStreamChannel(), sender + ", T'as deja votÃ©, triche pas ^^", sender);
                }
                return;
        }
        if (bot.getStream().isUserOp(sender)) {
            switch (cmd) {
                case "!result":
                    if (!votebanInprogress) {
                        bot.sendText(cmd, "pas de voteban en cours", sender);
                        return;
                    }
                    bot.sendText(bot.getStreamChannel(), "Calcul en cours...", sender);
                    Iterator<Entry<String, Boolean>> it = votes.entrySet().iterator();
                    int oui = 0,
                    non = 0;
                    while (it.hasNext()) {
                        Entry<String, Boolean> entry = it.next();
                        if (entry.getValue()) oui++;
                        else non++;
                    }
                    bot.sendText(bot.getStreamChannel(), "Le chat a votÃ© " + (oui > non ? "OUI a " + 100 * (oui / (oui + non)) + "%" : "NON a " + 100 * (non / (oui + non)) + "%") + " pour le cas de " + selected, sender);
                    if (oui > non) bot.sendTimeout(sender, toTime);
                    selected = null;
                    votebanInprogress = false;
                    return;
                case "!cancel":
                    bot.sendText(bot.getStreamChannel(), "vote annulÃ©e par [@s]" , sender);
                    selected = null;
                    votebanInprogress = false;
                    return;
                case "!cmdlist":
                    StreamerData stream = StreamerData.getStreamerData(bot.getStreamChannel());
                    bot.sendText(bot.getStreamChannel(), "commandes communes : " + this.getCommandsList(), sender);
                    bot.sendText(bot.getStreamChannel(), "commandes chez " + stream.getName() + " : " + stream.getCommandsList(), sender);
                    return;
                case "!randadd":
                    if (singleCmd) {
                        bot.sendText(bot.getStreamChannel(), "arguments manquants", sender);
                    } else if (msgArray[1].equalsIgnoreCase("join")) {
                        if (RandomText.addRanJoin(arrayToString(msgArray, 2))) bot.sendText(bot.getStreamChannel(), "Fait", sender);
                    } else if (msgArray[1].equalsIgnoreCase("leave")) {
                        if (RandomText.addRanLeave(arrayToString(msgArray, 2))) bot.sendText(bot.getStreamChannel(), "Fait", sender);
                    } else {
                        if (RandomText.addRanSentence(arrayToString(msgArray, 1))) bot.sendText(bot.getStreamChannel(), "Fait", sender);
                    }
                    Main.load();
                    return;
                case "!oplist":
                    String masters = "Maitre du Bot : ";
                    ArrayList<String> master = getUsers().getMasterUsers();
                    if (!master.isEmpty()) for (String line : master) {
                        masters += line + " | ";
                    }
                    else masters += "Aucun :(";
                    String modos = "Modo(s) du bot detectÃ©(s) : ";
                    ArrayList<String> modo = getUsers().getOpUsers();
                    if (!modo.isEmpty()) for (String line : modo) {
                        modos += line + " | ";
                    }
                    else modos += "Aucun :(";
                    
                    bot.sendText(bot.getStreamChannel(), masters, sender);
                    bot.sendText(bot.getStreamChannel(), modos, sender);
                    return;
                case "!rainbow" :
                    try{
                        bot.sendRainbow(bot.getStreamChannel(), TwitchColor.getTwitchColor(msgArray[1]));
                    }catch(IndexOutOfBoundsException e){
                        bot.sendRainbow(bot.getStreamChannel() , Main.defColor);
                    }
                    return;
                case "!canceltimeout" :
                    try{
                        bot.cancelTimeout(msgArray[1]);
                    }catch(IndexOutOfBoundsException e){
                        bot.sendText(bot.getStreamChannel(), "[@s] :  manque un nom FailFish ", sender);
                    }
                    return;
            }
        }
    }
    
    private static String arrayToString(String[] array, int startIndex) {
        String s = "";
        for (int i = startIndex; i < array.length; i++) {
            String word = array[i];
            s += word + " ";
        }
        return s;
    }


    @Override
    protected void generateSubCmds() {}
}
