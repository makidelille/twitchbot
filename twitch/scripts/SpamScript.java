package twitch.scripts;

import java.util.HashMap;

import twitch.bots.Bot;
import twitch.data.userData.Timelog;


public class SpamScript extends Script{

    private int MAXMPM = 15;
    private boolean isActive = true;
    private HashMap<String, Timelog> userTime = new HashMap<String, Timelog>();

    
    
    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(msg.startsWith("!")) if(configure(bot,sender,msg)) return true;
        if(!isActive) return false;
        if(bot.getStream().isUserOp(sender)) return false;
        if(!userTime.containsKey(sender.toLowerCase())){
            userTime.put(sender.toLowerCase(), new Timelog());
            return false;
        }
        userTime.get(sender.toLowerCase()).addNewTime(System.currentTimeMillis());
        int mpm = userTime.get(sender.toLowerCase()).getMessagePerMinute();
        if(mpm > MAXMPM){
            bot.timeout(sender);
            return true;
        }
        
        
        return false;
    }


    private boolean configure(Bot bot, String sender, String msg) {
        if(!bot.getStream().isUserOp(sender)) return false;
        String[] array = msg.split(" ");
        if(!array[0].equalsIgnoreCase("!spam")) return false;        
        switch(array[1]) {
            case "set" :
                try{
                    MAXMPM = Integer.valueOf(array[2]);
                    bot.sendText(bot.getStreamChannel(), "antispam, max mpm : " + MAXMPM, sender);
                }catch(IndexOutOfBoundsException | NumberFormatException e){
                    e.printStackTrace();
                    bot.sendText(bot.getStreamChannel(), "erreur dans la commande !", sender);
                }
                return true;
            case "on" :
                isActive = true;
                bot.sendText(bot.getStreamChannel(), "antispam actif,  max mpm : " + MAXMPM, sender);
                return true;
            case "off" :
                isActive = false;
                bot.sendText(bot.getStreamChannel(), "antispam inactif", sender);
                return true;
        }
        //erreur dans les args de la commande envoyé
        bot.sendText(bot.getStreamChannel(), "erreur dans la commande !", sender);
        return true;
    }


}
