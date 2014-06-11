
package twitch;

import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

import twitch.bots.Bot;
import twitch.lib.RandomText;
import twitch.lib.StreamerData;


public class Main {
    public static final String APPDATA= System.getenv("appdata")+ "/twitchbot/";

    private static String      pass;
    private static String      mode;
    public static long         startTime;
    public static final String MASTER  = "makidelille";
    public static final String MASTERCHANNEL = "#" + MASTER;
    
    public static void main(String[] args) throws Exception {
        startTime = System.currentTimeMillis();
        log("START");
        Scanner sc;
        if (args.length == 0) {
            System.out.println("arguments missing");
            System.out.println("please type you oauth key");
            sc = new Scanner(System.in);
            pass = sc.nextLine();
            if (!pass.startsWith("oauth") && !pass.equalsIgnoreCase("d")) {
                sc.close();
                return;
            }if(!pass.equalsIgnoreCase("d")) {
            mode = sc.nextLine().toUpperCase();
                if (!mode.equals("FALSE")) {
                    sc.close();
                    mode = "TRUE";
                }
            }
            sc.close();
        } else if (args.length == 1) {
            pass = args[0];
            mode = "TRUE";
        } else {
            pass = args[0];
            mode = args[1];
        }
        
        Main.load();
        
        if(pass.equalsIgnoreCase("d")) return;
        
        Bot bot = new Bot(MASTER, Boolean.valueOf(mode));
        bot.setVerbose(false);
        bot.connect("irc.twitch.tv", 6667, pass);
        bot.joinChannel(MASTERCHANNEL);
        log("RW BOT LOADED");
    }
    
    public static void log(String string) {
        Date time = new Date();
        String date = DateFormat.getInstance().format(time);
        System.out.println("[" + date + "] " + string);
    }
    
    public static String getTimeString(){
        return "[" + DateFormat.getInstance().format(new Date()) + "] ";
    }

    public static void load() {
        StreamerData.load();
        RandomText.load();        
    }
}
