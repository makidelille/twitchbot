
package twitch;

import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

import twitch.bots.Bot;
import twitch.data.streamData.StreamerData;
import twitch.scripts.Script;
import twitch.util.RandomText;
import twitch.util.TwitchColor;


public class Main {
    
    public static final String APPDATA       = System.getenv("appdata") + "/twitchbot/";
    private static String      pass;
    private static String      mode;
    public static long         startTime;
    public static final String MASTER        = "makidelille";
    public static final String MASTERCHANNEL = "#" + MASTER;
    public static final TwitchColor defColor = TwitchColor.BLUE;
    
    public static void main(String[] args) throws Exception {
        startTime = System.nanoTime();
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
        try {
            bot.connect("irc.twitch.tv", 6667, pass);
        } catch (ConnectException e) {
            log("connection error");
            log("stopping bots");
            bot.dispose();
            e.printStackTrace();
            stop();
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
        int dif =(int) ((System.nanoTime() - startTime)/Math.pow(10, 9));
        log("EXECUTION TIME : " + dif + "s");
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
        //TODO add Api
        StreamerData.load();
        Script.load();
        RandomText.load();
    }
}
