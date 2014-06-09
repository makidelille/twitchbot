package twitch;

import lib.Monstro99;
import lib.StreamerData;

public class Main {
	
	private static String pass;
	private static String channel;
	public static StreamerData monstro99 = new Monstro99();
	
	public static void main(String[] args) throws Exception {
		
		if(args.length < 2) {
			System.out.println("args missing");
			return;
		}
		
		pass = args[0];
		channel = args[1];
		
//		StreamerData monstro99 = new Monstro99();
//		monstro99.init();
//		Makidelille.init();
		
		
		Bot bot = new Bot("makidelille");
		bot.setVerbose(true);		
		bot.connect("irc.twitch.tv", 6667, pass);			
		bot.joinChannel("#" + channel);
		
	}
}
