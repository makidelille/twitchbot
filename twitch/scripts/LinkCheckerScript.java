
package twitch.scripts;

import twitch.bots.Bot;


public class LinkCheckerScript extends Script {
    
    public static final String[] exts = { "biz", "club", "com", "net", "org", "pro", "sexy", "xxx" };
    
    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(sender.equalsIgnoreCase("makidelille")) return false;
        //if (bot.getStream().isUserOp(sender)) return false;
        String text = removeSpace(msg);
        if (text.contains("http") || text.contains("www.") || isTLD(msg)) {
            System.out.println("lien");
        }
        return true;
    }
    
    private static String removeSpace(String msg) {
        String[] msgArray = msg.split(" ");
        String text = "";
        for (String line : msgArray) {
            text += line.toLowerCase();
        }
        return text;
    }
    
    public static boolean isTLD(String text) {
        if (!text.contains(".")) return false;
        String[] array = text.split(" ");
        for (String line : array) {
            if (line.contains(".")) {
                String[] subLine = line.split("\\.");
                if (subLine.length < 2) continue;
                String last = subLine[subLine.length - 1];
                if (last.length() == 2) return true;
                for (String ext : exts) {
                    if (last.equals(ext)) return true;
                }
            }
        }
        return false;
    }
}
