package lib;

import java.util.Iterator;
import java.util.Map.Entry;

import twitch.Main;

public class Lib {

	public static String getCfg(String channel, String piece) {
		String re ="erreur";
		String key,value;
		
		//TODO
		
		if (channel.equalsIgnoreCase("#monstro99")){
			if(Main.monstro99.getCfg().containsKey(piece.toUpperCase())) {
				re = piece.toUpperCase() + " : " + Main.monstro99.getCfg().get(piece);
			}else if(piece == "*") {
				Iterator<Entry<String, String>> it = Main.monstro99.getCfg().entrySet().iterator();
				while(it.hasNext()) {
					re += it.toString().toUpperCase() + Main.monstro99.getCfg().get(it).toString();
				}
			}
		}
//		else if(channel.equalsIgnoreCase("#makidelille")){
//			if(Makidelille.cfg.containsKey(piece.toUpperCase())) {
//				re = piece.toUpperCase() + " : " +  Makidelille.cfg.get(piece);
//			}else if(piece == "*") {
//				System.out.println("all");
//				Iterator<Entry<String, String>> it = Makidelille.cfg.entrySet().iterator();
//				while(it.hasNext()) {
//					Entry<String, String> entry = (Entry<String, String>) it.next();
//					key = entry.getKey();
//					value = entry.getValue();
//					re += key.toUpperCase() + " : " + value;
//				}
//			}
//		}
		return re;
	}
	
	public static String getResolution(String channel) {
		switch(channel) {
		case "#monstro99" : return Main.monstro99.getResolution();
		case "#makidelille" : return Makidelille.resolution;
		default : return "unknown";
		}
	}
	
}
