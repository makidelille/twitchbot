package twitch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import lib.Lib;
import lib.StreamerData;

import org.jibble.pircbot.PircBot;

public class Bot extends PircBot{
	public boolean isPaused;

	private String selected;

	private boolean votebanInprogress;
	private HashMap<String, Boolean> votes;
	private ArrayList<String> modo;

	private boolean debugMode = false;
	private boolean silentMode = false;
	
	private boolean forceQuit = false;
	
	public Bot(String name, boolean silentMode) {
		this.setName(name);
		this.setMessageDelay(10);
		this.isPaused = false;
		this.selected = "";
		this.votes = new HashMap<String, Boolean>();
		this.modo = new ArrayList<String>();
		this.silentMode = silentMode;
	}
	
	//TODO implements cmd level
	
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String msg) {		
		System.out.println(this.isUserOp(channel, sender));
		
		if(isPaused && !sender.equalsIgnoreCase("makidelille")) return;
		//line to interrupt any cmd if bot is in debug mode
		if(debugMode  && !sender.equalsIgnoreCase("makidelille") && msg.startsWith("!")) {
			sendMessage(channel, "[Bot] hum... NOPE");
			return;		
		}
		super.onMessage(channel, sender, login, hostname, msg);
		
		if(msg.toLowerCase().contains("hodor")) {
			sendMessage(channel, "/me HODOR");
			return;
		}
		if(msg.contains("Xd") && !sender.equalsIgnoreCase("makidelille")) {
			sendMessage(channel, "[Bot] Toi aussi tu sais pas les faire Xd Kappa");
			return;
		}
		
		String[] msgArray = msg.split(" ");
		String cmd = msgArray[0].toLowerCase();
		
		boolean singleCmd = msgArray.length == 1;
		
		switch(cmd) {
		case "!ping" :
			if(sender.equalsIgnoreCase("makidelille")) {
				sendMessage(channel, "[Bot] pong");
			}
			return;
		case "!delay":
			if(sender.equalsIgnoreCase("makidelille")) {
				if(singleCmd) {
					sendMessage(channel, "[Bot] Refresh Rate set to " + this.getMessageDelay() + "ms");
				}else{
					int time = Integer.valueOf(msgArray[1]);
					this.setMessageDelay(time);
					sendMessage(channel, "[Bot] Refresh Rate set to " + time + "ms");
				}
			}
			return;
		case "!leave" :
			if(sender.equalsIgnoreCase("makidelille")) {
				sendMessage(channel, "[Bot] Bot leaving and returning home ...");
				this.partChannel(channel);
				this.joinChannel("#makidelille");
			}
			return;
		case "!quit" :
			if(sender.equalsIgnoreCase("makidelille")) {
				this.quitServer();
				this.log("succesful leave");
				this.forceQuit  = true;
			}
			return;
		case "!debug" : 
			if(sender.equalsIgnoreCase("makidelille")) {
				debugMode = !debugMode;
				sendMessage(channel, "[Bot] Debug Mode : " + (debugMode ? "On" : "Off"));
			}
			return;
		case "!pause" :
			if(sender.equalsIgnoreCase("makidelille")) {
				sendMessage(channel, "[Bot] Bot is paused ...");
				this.isPaused = true;
			}
			return;
		case "!join" : 
			if(sender.equalsIgnoreCase("makidelille") && !singleCmd) {
				sendMessage(channel, "[Bot] joining " + msgArray[1]);
				this.partChannel(channel);
				this.joinChannel("#" + msgArray[1]);
			}
			return;
		case "!resume" :
			if(sender.equalsIgnoreCase("makidelille")) {
				if(isPaused) sendMessage(channel, "[Bot] Bot is working ...");
				else sendMessage(channel, "[Bot] Bot is already working ...");
				this.isPaused = false;
			}
			return;
		case "!time" :
			String time = new Date().toString();
			sendMessage(channel, "[Bot] Local time : " + time); //TODO time translation
			return;
		case "!regle" :
			sendMessage(channel, "[Bot] C'est quoi ça ?");
			return;
		case "!cfg" :
			String cfg;
			if(singleCmd) {
				cfg = Lib.getCfg(channel, "*");
			}else{
				cfg = Lib.getCfg(channel, msgArray[1]);
			}
			sendMessage(channel, "[Bot] " + cfg);
			return;
		case "!controllers" :
			String controls;
			if(singleCmd) {
				controls = Lib.getControls(channel, "*");
			}else{
				controls = Lib.getControls(channel, msgArray[1]);
			}
			sendMessage(channel, "[Bot] " + controls);
			return;
		case "!res" :
			sendMessage(channel, "[Bot] " + Lib.getResolution(channel));
			return;
		case "!lien" :
			if(singleCmd){
				sendMessage(channel, "[Bot] Tu peux poster des liens");
				return;
			}
			String querry = msg.substring(cmd.length() +1).replace(" ", "+");
			sendMessage(channel, "[Bot] lmgtfy.com/?q=" + querry);
			return;
		case "!votetimeout" : 
			if(singleCmd) return;
			selected = msgArray[1];
			sendMessage(channel, "[Bot] " +sender + " veux timeout " + selected);
			sendMessage(channel, "[Bot] !oui ou !non ?");
			votebanInprogress = true;
			votes = new HashMap<String, Boolean>();
			return;
		case "!oui" :
			if(!votebanInprogress){
				sendMessage(channel, "[Bot] Pas de !votetimeout en cours :D");
				return;
			}
			if(!votes.containsKey(sender.toLowerCase())) {
				votes.put(sender.toLowerCase(), true);
				sendMessage(channel, "[Bot] "+ sender +", Vote enregistrer");
			}else{
				sendMessage(channel, "[Bot] "+ sender +", T'as deja voter triche pas ^^");
			}
			return;
		case "!non" :
			if(!votebanInprogress){
				sendMessage(channel, "[Bot] Pas de !votetimeout en cours :D");
				return;
			}
			if(!votes.containsKey(sender.toLowerCase())) {
				votes.put(sender.toLowerCase(), false);
				sendMessage(channel, "[Bot] "+ sender +", Vote enregistrer");
			}else{
				sendMessage(channel, "[Bot] "+ sender +", T'as deja voter triche pas ^^");
			}
			return;
		case "!result" :
			if(!votebanInprogress) return;
			sendMessage(channel, "[Bot] Calcul en cours...");
			Iterator<Entry<String, Boolean>> it = votes.entrySet().iterator();
			int oui = 0,non = 0;
			while(it.hasNext()) {
				Entry<String, Boolean> entry = it.next();
				if(entry.getValue()) oui++;
				else non++;
			}
			sendMessage(channel, "[Bot] Le chat a voter " + (oui > non ? "OUI a " + 100 * (oui/(oui + non)) + "%" : "NON a "+ 100 * (non/(oui + non)) + "%" ) + " pour le cas de " + selected) ;
		//	sendMessage(target, message); //TODO timeout
			votebanInprogress = false;
			return;
		}
		String special = StreamerData.getChannelWithCmd(cmd);
		if(special != null) StreamerData.map.get(special).onSpecialCmd(this,sender, msg);  
	
	}
	
	
	@Override
	protected void onJoin(String channel, String sender, String login,String hostname) {
		super.onJoin(channel, sender, login, hostname);
		if(login.equalsIgnoreCase(this.getName())) {
			this.log("BOT READY");	
			if(!silentMode) sendMessage(channel, "Hello les gens, c'est le [Bot] ou pas ?");
		}
		//sendMessage(channel, "bonjour " + login);
	}
	
	@Override
	protected void onUserMode(String targetNick, String sourceNick,
			String sourceLogin, String sourceHostname, String mode) {
		super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);
		System.out.println(sourceNick); //TODO work here
	}
	
	private boolean isUserOp(String channel,String userToCompare) {
		for(String user : modo) {
			if(user.equals(userToCompare)) return true;
		}
		return false;
	}
	
	
}
