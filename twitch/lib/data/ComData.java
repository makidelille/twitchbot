
package twitch.lib.data;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import twitch.bots.Bot;
import twitch.lib.RandomText;
import twitch.lib.StreamerData;


public class ComData extends StreamerData {
    
    private boolean                  votebanInprogress;
    private HashMap<String, Boolean> votes;
    private String                   selected;
    private Integer                  toTime;
    
    public ComData() {
        super("twitch");
    }
    
    @Override
    public void init() {
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
                bot.sendText(bot.getStreamChannel(), "Local time : " + date);
                return;
            case "!lien":
                if (singleCmd) { return ; }
                String querry = msg.substring(cmd.length() + 1).replace(" ",
                        "+");
                bot.sendText(bot.getStreamChannel(), "lmgtfy.com/?q=" + querry);
                return ;
            case "!random" :
                if(singleCmd){
                    bot.sendText(bot.getStreamChannel(), RandomText.getRanSentence());
                    return;
                }
                if(msgArray[1].equalsIgnoreCase("join")) {
                    bot.sendText(bot.getStreamChannel(), "@s : " + RandomText.getRanJoin(), sender);
                    return;
                }
                if(msgArray[1].equalsIgnoreCase("leave")) {
                    bot.sendText(bot.getStreamChannel(), "@s : " + RandomText.getRanLeave(), sender);
                    return;
                }
                try{
                    int value = Integer.valueOf(msgArray[1]);
                    Random rand = new Random();
                    bot.sendText(bot.getStreamChannel(), "" + rand.nextInt(value));
                }catch(NumberFormatException e){
                    e.printStackTrace();
                    bot.sendText(bot.getStreamChannel(), "paramatre non valide");
                }return;
            case "!votetimeout":
                if (singleCmd) return ;
                selected = msgArray[1];
                try {
                    toTime = Integer.valueOf(msgArray[2]);
                } catch (NumberFormatException e) {
                    bot.sendText(bot.getStreamChannel(),
                            "temps invalide, valeur par default = 300sec");
                    toTime = 300;
                }
                bot.sendText(bot.getStreamChannel(), sender + " veux timeout " + selected);
                bot.sendText(bot.getStreamChannel(), "!oui ou !non ?");
                votebanInprogress = true;
                votes = new HashMap<String, Boolean>();
                return ;
            case "!oui":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D");
                    return ;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), true);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistré");
                } else {
                    bot.sendText(bot.getStreamChannel(), sender
                            + ", T'as deja voté, triche pas ^^");
                }
                return ;
            case "!yes":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D");
                    return ;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), true);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistré");
                } else {
                    bot.sendText(bot.getStreamChannel(), sender
                            + ", T'as deja voté, triche pas ^^");
                }
                return ;
            case "!non":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D");
                    return ;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), false);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistré");
                } else {
                    bot.sendText(bot.getStreamChannel(), sender
                            + ", T'as deja voté, triche pas ^^");
                }
                return ;
            case "!no":
                if (!votebanInprogress) {
                    bot.sendText(bot.getStreamChannel(), "Pas de !votetimeout en cours :D");
                    return ;
                }
                if (!votes.containsKey(sender.toLowerCase())) {
                    votes.put(sender.toLowerCase(), false);
                    bot.sendText(bot.getStreamChannel(), sender + ", Vote enregistré");
                } else {
                    bot.sendText(bot.getStreamChannel(), sender
                            + ", T'as deja voté, triche pas ^^");
                }
                return ;
        }
        if (this.isUserOp(bot.getStreamChannel(), sender)) {
            switch (cmd) {
                case "!result":
                    if (!votebanInprogress) {
                        bot.sendText(cmd, msg);
                        return ;
                    }
                    bot.sendText(bot.getStreamChannel(), "Calcul en cours...");
                    Iterator<Entry<String, Boolean>> it = votes.entrySet()
                            .iterator();
                    int oui = 0,
                    non = 0;
                    while (it.hasNext()) {
                        Entry<String, Boolean> entry = it.next();
                        if (entry.getValue()) oui++;
                        else non++;
                    }
                    bot.sendText(bot.getStreamChannel(), "Le chat a voter "
                            + (oui > non ? "OUI a " + 100 * (oui / (oui + non))
                                    + "%" : "NON a " + 100
                                    * (non / (oui + non))
                                    + "%") + " pour le cas de " + selected);
                    bot.sendMessage(bot.getStreamChannel(), "/timeout " + selected + " "
                            + toTime);
                    selected = null;
                    votebanInprogress = false;
                    return;
                case "!cancel":
                    bot.sendText(bot.getStreamChannel(), "vote annul�e par " + sender);
                    selected = null;
                    votebanInprogress = false;
                    return;
                case "!cmdlist":
                    StreamerData stream = StreamerData.getStreamerData(bot.getStreamChannel());
                    bot.sendText(bot.getStreamChannel(),"commandes communes : " + this.getCommandsList());
                    bot.sendText(bot.getStreamChannel(), "commandes chez " + stream.getName() +" : " + stream.getCommandsList());
                    return;
                case "!randadd" :
                    if(singleCmd || msgArray.length < 2){
                        bot.sendText(bot.getStreamChannel(), "arguments manquants");
                        return;
                    }else if(msgArray[1].equalsIgnoreCase("join")){
                        if(RandomText.addRanJoin(arrayToString(msgArray, 2)))
                            bot.sendText(bot.getStreamChannel(), "Fait");
                        return;
                    }else if(msgArray[1].equalsIgnoreCase("leave")){
                        if(RandomText.addRanLeave(arrayToString(msgArray, 2)))
                            bot.sendText(bot.getStreamChannel(), "Fait");
                        return;
                    }else if(msgArray[1].equalsIgnoreCase("sentence")){
                        if(RandomText.addRanSentence(arrayToString(msgArray, 2)))
                            bot.sendText(bot.getStreamChannel(), "Fait");
                        return;
                    }
                    
                    
                    
                    
                    return;
            }
        }
    }
    
    private static String arrayToString(String[] array,int startIndex) {
        String s = "";
        for(int i = startIndex; i < array.length; i++){
            String word = array[i];
            s += word + " ";
        }
        return s;
    }
}