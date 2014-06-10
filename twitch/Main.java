
package twitch;

import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

import twitch.lib.StreamerData;


public class Main {
    
    private static String      pass;
    private static String      mode;
    private static String      channel = "makidelille";
    public static long         startTime;
    public static final String MASTER  = "makidelille";
    
    public static void main(String[] args) throws Exception {
        startTime = System.currentTimeMillis();
        log("START");
        Scanner sc;
        if (args.length == 0) {
            System.out.println("arguments missing");
            System.out.println("please type you oauth key");
            sc = new Scanner(System.in);
            pass = sc.nextLine();
            if (!pass.startsWith("oauth")) {
                sc.close();
                return;
            }
            mode = sc.nextLine().toUpperCase();
            if (!mode.equals("FALSE")) {
                sc.close();
                mode = "TRUE";
            }
            sc.close();
        } else if (args.length == 1) {
            pass = args[0];
            mode = "TRUE";
        } else {
            pass = args[0];
            mode = args[1];
        }
        StreamerData.load();
        Bot bot = new Bot("makidelille", Boolean.valueOf(mode));
        bot.setVerbose(true);
        bot.connect("irc.twitch.tv", 6667, pass);
        bot.joinChannel("#" + channel);
        log("bot is running");
    }
    
    public static void log(String string) {
        Date time = new Date();
        String date = DateFormat.getInstance().format(time);
        System.out.println("[" + date + "] " + string);
    }
}
