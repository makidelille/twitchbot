package lib;

import java.util.ArrayList;
import java.util.HashMap;

import org.jibble.pircbot.PircBot;

public class Monstro99 extends StreamerData{

	public HashMap<String,String> cfg;
	public HashMap<String,String> controls;
	public ArrayList<String> cmds;
	public String resolution = "1280x800 @120Hz";
	
	public Monstro99() {
		super("monstro99");
	}
	
	public void init() {
		cfg = new HashMap<String, String>();
		cfg.put("CPU", "I7 860+@4.2Ghz Corsair H50 + 2x Cooler Master Excalibur");
		cfg.put("RAM", "8Go Crucial Ballistix pc16000 (2000Mhz)");
		cfg.put("GPU", "MSI GTX 770 OC 2Go Gaming");
		cfg.put("HDD", "1x250 Go 16Mo / Raid0 2x750Go 32Mo");
		cfg.put("MB" , "EVGA P55 FTW");
		cfg.put("SOUND CARD", "X-Fi sound blaster XG fatal1ty");
		cfg.put("PSU", "Corsair HX 850");
		
		controls = new HashMap<String, String>();
		controls.put("MOUSE", "Logitech G500");
		controls.put("MOUSE PAD" , "");
		controls.put("KEYBOARD", "Logitech G510");
		controls.put("HEADSET", "Steelseries 9H + FLUX");
		controls.put("PAD","XBOX 360 gamepad");
		
		cmds = new ArrayList<String>();
		cmds.add("!avion");

	}
	
	@Override
	public HashMap<String, String> getCfg() {
		return this.cfg;
	}
	
	@Override
	public HashMap<String, String> getControllers() {
		return this.controls;
	}
	
	@Override
	public String getResolution() {
		return resolution;
	}
	
	@Override
	public ArrayList<String> getCmds() {
		return cmds;
	}
	
	@Override
	public void onSpecialCmd(PircBot bot, String sender, String msg) {
		String[] msgArray = msg.split(" ");
		String cmd = msgArray[0].toLowerCase();
		
		switch(cmd) {
		case "!avion" : 
			bot.sendMessage(this.channel, "[Bot] Monstro joue avec une manette Xbox 360 pour les vehicules aeriens");
			return;
		}
	}
	
}
