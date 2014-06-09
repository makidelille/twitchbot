package twitch;

import java.util.Scanner;

import lib.Lib;


public class Main {
	
	private static String pass;
	private static String mode;
	private static String channel = "makidelille";
	
	public static void main(String[] args) throws Exception {
		
		Scanner sc;
		
		if(args.length == 0) {
			System.out.println("arguments missing");
			System.out.println("please type you oauth key");
			sc = new Scanner(System.in);
			pass = sc.nextLine();
			if(!pass.startsWith("oauth")) {
				sc.close();
				return;
			}
			mode = sc.nextLine().toUpperCase();
			if(!mode.equals("TRUE") && !mode.equals("FALSE")) {
				sc.close();
				return;
			}
			sc.close();
		}else if(args.length == 1){			
			pass = args[0];
			sc = new Scanner(System.in);
			mode = sc.nextLine().toUpperCase();
			if(!mode.equals("TRUE") && !mode.equals("FALSE")) {
				sc.close();
				return;
			}
			sc.close();
		}else{
			pass = args[0];
			mode = args[1];
		}
		
		Lib.init();
		
		Bot bot = new Bot("makidelille", Boolean.valueOf(mode));
		bot.setVerbose(true);		
		bot.connect("irc.twitch.tv", 6667, pass);			
		bot.joinChannel("#" + channel);
		
		
		//TODO implements the stop ^^
	}
}
