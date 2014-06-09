package lib;

import java.util.Iterator;
import java.util.Map.Entry;

public class Lib {
	public static StreamerData monstro99;
	public static StreamerData makidelille ;

	public static void init() {
		monstro99 = new Monstro99();
		makidelille = new Makidelille();
	}
	
	public static String getCfg(String channel, String piece) {
		String re ="";
		String key,value;
	
		if (channel.equalsIgnoreCase("#monstro99")){
			if(monstro99.getCfg().containsKey(piece.toUpperCase())) {
				re = piece.toUpperCase() + " : " + monstro99.getCfg().get(piece.toUpperCase());
			}else if(piece == "*") {
				re = "";
				Iterator<Entry<String, String>> it = monstro99.getCfg().entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) it.next();
					key = entry.getKey();
					value = entry.getValue();
					re += key.toUpperCase() + " : " + value + "; ";
				}
			}else{
				re = "argument invalide";
			}
		}else if(channel.equalsIgnoreCase("#makidelille")){
			if(makidelille.getCfg().containsKey(piece.toUpperCase())) {
				re = piece.toUpperCase() + " : " +  makidelille.getCfg().get(piece.toUpperCase());
			}else if(piece == "*") {
				Iterator<Entry<String, String>> it = makidelille.getCfg().entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) it.next();
					key = entry.getKey();
					value = entry.getValue();
					re += key.toUpperCase() + " : " + value;
				}
			}else{
				re = "argument invalid";
			}
		}else{
			re = "no data";
		}
		return re;
	}
	
	public static String getResolution(String channel) {
		switch(channel) {
		case "#monstro99" : return monstro99.getResolution();
		case "#makidelille" : return makidelille.getResolution();
		default : return "unknown";
		}
	}
	
	public static boolean isUserOp(String user) {
		
		
		
		return false;
	}

	public static String getControls(String channel, String piece) {
		String re ="";
		String key,value;
	
		if (channel.equalsIgnoreCase("#monstro99")){
			if(monstro99.getCfg().containsKey(piece.toUpperCase())) {
				re = piece.toUpperCase() + " : " + monstro99.getControllers().get(piece.toUpperCase());
			}else if(piece == "*") {
				re = "";
				Iterator<Entry<String, String>> it = monstro99.getControllers().entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) it.next();
					key = entry.getKey();
					value = entry.getValue();
					re += key.toUpperCase() + " : " + value + "; ";
				}
			}else{
				re = "argument invalide";
			}
		}else if(channel.equalsIgnoreCase("#makidelille")){
			if(makidelille.getCfg().containsKey(piece.toUpperCase())) {
				re = piece.toUpperCase() + " : " +  makidelille.getControllers().get(piece.toUpperCase());
			}else if(piece == "*") {
				Iterator<Entry<String, String>> it = makidelille.getControllers().entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) it.next();
					key = entry.getKey();
					value = entry.getValue();
					re += key.toUpperCase() + " : " + value;
				}
			}else{
				re = "argument invalid";
			}
		}else{
			re = "no data";
		}
		return re;
	}
	
}
