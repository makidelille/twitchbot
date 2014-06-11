
package twitch.lib.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import twitch.bots.Bot;
import twitch.lib.RandomText;
import twitch.lib.StreamerData;


public class Monstro99Data extends StreamerData {
    
    public HashMap<String, String> cfg;
    public HashMap<String, String> controls;
    
    public Monstro99Data() {
        super("monstro99");
    }
    
    public void init() {
        cfg = new HashMap<String, String>();
        cfg.put("CPU", "I7 860+@4.2Ghz Corsair H50 + 2x Cooler Master Excalibur");
        cfg.put("RAM", "8Go Crucial Ballistix pc16000 (2000Mhz)");
        cfg.put("GPU", "MSI GTX 770 OC 2Go Gaming");
        cfg.put("HDD", "1x250 Go 16Mo / Raid0 2x750Go 32Mo");
        cfg.put("MB", "EVGA P55 FTW");
        cfg.put("SOUND CARD", "X-Fi sound blaster XG fatal1ty");
        cfg.put("PSU", "Corsair HX 850");
        controls = new HashMap<String, String>();
        controls.put("MOUSE", "Logitech G500");
        controls.put("MOUSE PAD", "");
        controls.put("KEYBOARD", "Logitech G510");
        controls.put("HEADSET", "Steelseries 9H + FLUX");
        controls.put("PAD", "XBOX 360 gamepad");
    }
    
    public String getCfg(String piece) {
        String re;
        String key, value;
        if (this.cfg.containsKey(piece.toUpperCase())) {
            re = piece.toUpperCase() + " : " + this.cfg.get(piece.toUpperCase());
        } else if (piece == "*") {
            re = "";
            Iterator<Entry<String, String>> it = this.cfg.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) it.next();
                key = entry.getKey();
                value = entry.getValue();
                re += key.toUpperCase() + " : " + value + "; ";
            }
        } else {
            re = "argument invalide";
        }
        return re;
    }
    
    public String getControllers(String piece) {
        String re, key, value;
        if (this.controls.containsKey(piece.toUpperCase())) {
            re = piece.toUpperCase() + " : " + this.controls.get(piece.toUpperCase());
        } else if (piece == "*") {
            re = "";
            Iterator<Entry<String, String>> it = this.controls.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) it.next();
                key = entry.getKey();
                value = entry.getValue();
                re += key.toUpperCase() + " : " + value + "; ";
            }
        } else {
            re = "argument invalide";
        }
        return re;
    }
    
    @Override
    public void onSpecialCmd(Bot bot, String sender, String msg) {
        String[] msgArray = msg.split(" ");
        String cmd = msgArray[0];
        boolean singleCmd = msg == cmd;
        switch (cmd) {
            case "!cfg":
                String cfg;
                if (singleCmd) {
                    cfg = this.getCfg("*");
                } else {
                    cfg = this.getCfg(msgArray[1]);
                }
                bot.sendText(channel, cfg, sender);
                return;
            case "!controls":
                String controls;
                if (singleCmd) {
                    controls = this.getControllers("*");
                } else {
                    controls = this.getControllers(msgArray[1]);
                }
                bot.sendText(channel, controls, sender);
                return;
            case "!rage":
                int par = Integer.valueOf(msgArray[1]);
                bot.sendText(channel, sender + " : " + RandomText.getRandomString(par));
                return;
        }
    }
}
