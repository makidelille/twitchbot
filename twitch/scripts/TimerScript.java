
package twitch.scripts;

import java.util.ArrayList;

import twitch.bots.Bot;


public class TimerScript extends Script {
    
    private ArrayList<String> msgs = new ArrayList<String>();
    private boolean isActive = true;
    private boolean hasInit;
    private long lastMsgTime;
    private long timeDif = 10 * 60 * 1000;
    private int msgSentSinceLast;
    private int msgDif = 30;
    private int indCurMsg;
    
    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if (!hasInit) {
            load();
            hasInit = true;
        }
        if (msg.startsWith("!")) configure(bot, msg, sender);
        if (!isActive) return false;
        if (msgs.isEmpty()) return false;
        msgSentSinceLast++;
        long current = System.currentTimeMillis();
        if (current - lastMsgTime < timeDif) return false;
        if (msgSentSinceLast < msgDif) return false;
        sendNextMessage(bot);
        msgSentSinceLast = 0;
        lastMsgTime = System.currentTimeMillis();
        return false;
    }
    
    private void configure(Bot bot, String msg, String sender) {
        if (!bot.getStream().isUserOp(sender)) return;
        String[] array = msg.split(" ");
        if (!array[0].equalsIgnoreCase("!spam")) return;
        switch (array[1]) {
            case "setTime":
                try {
                    timeDif = Integer.valueOf(array[2]) * 1000; // time is in
                                                                // sec
                    bot.sendText(bot.getStreamChannel(), "intervalle de temps mini : " + timeDif / 1000 + "s", sender);
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                    bot.sendText(bot.getStreamChannel(), "erreur dans la commande !", sender);
                }
                return;
            case "setDif":
                try {
                    msgDif = Integer.valueOf(array[2]);
                    bot.sendText(bot.getStreamChannel(), "nombre de message mini entre deux envoie : " + msgDif, sender);
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                    bot.sendText(bot.getStreamChannel(), "erreur dans la commande !", sender);
                }
                return;
            case "on":
                isActive = true;
                bot.sendText(bot.getStreamChannel(), "envoie de message régulier ", sender);
                return;
            case "off":
                isActive = false;
                bot.sendText(bot.getStreamChannel(), "pas de message régulier", sender);
                return;
        }
        // erreur dans les args de la commande envoy�
        bot.sendText(bot.getStreamChannel(), "erreur dans la commande !", sender);
    }
    
    private void sendNextMessage(Bot bot) {
        lastMsgTime = System.currentTimeMillis();
        String msg = msgs.get(indCurMsg);
        bot.sendText(bot.getStreamChannel(), msg, bot.getStream().getName());
        indCurMsg = indCurMsg < msgs.size() - 1 ? indCurMsg++ : 0;
    }
    
    private void load() {
    }
}
