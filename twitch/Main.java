
package twitch;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

import twitch.bots.Bot;
import twitch.lib.RandomText;
import twitch.lib.StreamerData;


public class Main {
    
    public static final String APPDATA       = System.getenv("appdata") + "/twitchbot/";
    private static String      pass;
    private static String      mode;
    public static long         startTime;
    public static final String MASTER        = "makidelille";
    public static final String MASTERCHANNEL = "#" + MASTER;
    
    public static void main(String[] args) throws Exception {
        startTime = System.currentTimeMillis();
        log("        START");
        log("=======================");
        Scanner sc;
        if (args.length == 0) {
            System.out.println("arguments missing");
            System.out.println("please type you oauth key");
            sc = new Scanner(System.in);
            pass = sc.nextLine();
            if (!pass.startsWith("oauth") && !pass.equalsIgnoreCase("d")) {
                sc.close();
                return;
            }
            if (!pass.equalsIgnoreCase("d")) {
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
        if (!pass.startsWith("oauth")) {
            log("======================");
            log("NOT A VALIDE OAUTH KEY");
            log("  BOT WILL NOT START");
            log("======================");
            stop();
        }
        
        Bot bot = new Bot(MASTER, Boolean.valueOf(mode));
        bot.setVerbose(false);
        try{
            bot.connect("irc.twitch.tv", 6667, pass);            
        }catch(ConnectException e) {
            log("connection error");
            log("stopping bots");
            bot.dispose();
            e.printStackTrace();
        }
        
        bot.joinChannel(MASTERCHANNEL);
        log("RW BOT LOADED");
    }
    
    public static void log(String string) {
        Date time = new Date();
        String date = DateFormat.getInstance().format(time);
        System.out.println("[" + date + "] " + string);
    }
    
    public static void stop() {
        Time dif = new Time(System.currentTimeMillis() - startTime);
        log("EXECUTION TIME : " + dif);
        log("=======================");
        log("=======================");
        log("        END");
        try {
            System.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.close();
        System.exit(0);
        return;
    }
    
    public static String getTimeString() {
        return "[" + DateFormat.getInstance().format(new Date()) + "] ";
    }
    
    public static void load() {
        StreamerData.load();
        RandomText.load();
    }
}
