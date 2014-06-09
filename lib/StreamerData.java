package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jibble.pircbot.PircBot;


public class StreamerData {
	
	public static HashMap<String, StreamerData >map = new HashMap<String, StreamerData>();
	protected String channel;
	
	public  StreamerData(String channel) {
		this.init();
		this.channel = "#" + channel;
		map.put(channel, this);
	}
	
	public void init() {}

	public HashMap<String, String> getCfg() {
		return null;
	}

	public String getResolution() {
		return null;
	}
	
	public HashMap<String,String> getControllers() {
		return null;
	}
	
	public ArrayList<String> getCmds() {
		return new ArrayList<String>();
	}
	
	public void onSpecialCmd(PircBot bot, String sender, String msg){}
	public boolean isChannelSpecialCmd(String cmd) {
		return this.getCmds().contains(cmd.toLowerCase());
	}
	
	public static String getChannelWithCmd(String cmd) {
		Iterator<Entry<String, StreamerData>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, StreamerData> entry = it.next();
			if(entry.getValue().isChannelSpecialCmd(cmd)) return entry.getKey();
		}
		return null;
		
	}
		
}

