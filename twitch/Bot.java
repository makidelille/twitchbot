package twitch;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import lib.Lib;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class Bot extends PircBot{
	public static final int TIMEDEBUF = 10_000;

	public boolean isPaused;
	public int timeSincelast;

	private String selected;

	private boolean votebanInprogress;
	private HashMap<String, Boolean> votes;
	
	public Bot(String name) {
		this.setName(name);
		this.isPaused = false;
		this.timeSincelast = 0;
		this.selected = "";
		this.votes = new HashMap<String, Boolean>();
	}
	
	
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String msg) {		
		if(isPaused && !sender.equalsIgnoreCase("makidelille")) return;
		//DEBUG LINE //TODO remove when it'll be in use
//		if(!sender.equalsIgnoreCase("makidelille") && msg.startsWith("!")) {
//			sendMessage(channel, "hum... NOPE");
//			return;		
//		}
		super.onMessage(channel, sender, login, hostname, msg);
		
		if(msg.toLowerCase().contains("hodor")) {
			sendMessage(channel, "/me HODOR");
			return;
		}
		if(msg.contains("Xd") && !sender.equalsIgnoreCase("makidelille")) {
			sendMessage(channel, "Toi aussi tu sais pas les faire Xd Kappa");
			return;
		}
		
		String[] msgArray = msg.split(" ");
		String cmd = msgArray[0].toLowerCase();
		
		boolean singleCmd = msgArray.length == 1;
		
		switch(cmd) {
		case "!ping" :
			if(sender.equalsIgnoreCase("makidelille")) {
				sendMessage(channel, "pong");
			}
			return;
		case "!leave" :
			if(sender.equalsIgnoreCase("makidelille")) {
				sendMessage(channel, "bot leaving... returning home ...");
				this.partChannel(channel);
				this.joinChannel("#makidelille");
			}
			return;
		case "!pause" :
			if(sender.equalsIgnoreCase("makidelille")) {
				sendMessage(channel, "bot is paused ...");
				this.isPaused = true;
			}
			return;
		case "!join" : 
			if(sender.equalsIgnoreCase("makidelille") && !singleCmd) {
				this.partChannel(channel);
				this.joinChannel("#" + msgArray[1]);
			}
			return;
		case "!resume" :
			if(sender.equalsIgnoreCase("makidelille")) {
				if(isPaused) sendMessage(channel, "bot is working ...");
				else sendMessage(channel, "bot is already working ...");
				this.isPaused = false;
			}
			return;
		case "!time" :
			String time = new Date().toString();
			sendMessage(channel, sender +  " :  il est " + time); //TODO time translation
			return;
		case "!regle" :
			sendMessage(channel, "c'est quoi ça ?");
			return;
		case "!love" :
			if(singleCmd) return;
			sendMessage(channel,"/me " + msgArray[1] + "<3");
			return;
		case "!cfg" :
			String cfg;
			if(singleCmd) cfg = Lib.getCfg(channel, "*");
			else cfg = Lib.getCfg(channel, msgArray[1]);
			sendMessage(channel, cfg);
			return;
		case "!res" :
			sendMessage(channel, Lib.getResolution(channel));
			return;
		case "!lien" :
			String querry = msg.substring(cmd.length() +1).replace(" ", "+");
			sendMessage(channel, "lmgtfy.com/?q=" + querry);
			return;
		case "!spam" :
			sendMessage(channel, "3...");
			sendMessage(channel, "2...");
			sendMessage(channel, "1...");
			sendMessage(channel, "c'est parti!");
			return;
		case "!votetimeout" : 
			if(singleCmd) return;
			selected = msgArray[1];
			sendMessage(channel, sender + " veux timeout " + selected);
			sendMessage(channel, "!oui ou !non ?");
			votebanInprogress = true;
			votes = new HashMap<String, Boolean>();
			return;
		case "!oui" :
			if(!votebanInprogress) return;
			if(!votes.containsKey(sender.toLowerCase())) {
				votes.put(sender.toLowerCase(), true);
				sendMessage(channel, sender + " vote enregister");
			}else{
				sendMessage(channel, sender + " t'as deja voter triche pas ^^");
			}
			return;
		case "!non" :
			if(!votebanInprogress) return;
			if(!votes.containsKey(sender.toLowerCase())) {
				votes.put(sender.toLowerCase(), false);
				sendMessage(channel, sender + "vote enregister");
			}else{
				sendMessage(channel, sender + " t'as deja voter triche pas ^^");
			}
			return;
		case "!result" :
			if(!votebanInprogress) return;
			sendMessage(channel, "calcul en cours...");
			Iterator<Entry<String, Boolean>> it = votes.entrySet().iterator();
			int oui = 0,non = 0;
			while(it.hasNext()) {
				Entry<String, Boolean> entry = it.next();
				if(entry.getValue()) oui++;
				else non++;
			}
			sendMessage(channel, "le chat a voter " + (oui > non ? "oui a " + 100 * (oui/(oui + non)) + "%" : "non a "+ 100 * (non/(oui + non)) + "%" ) + " pour le cas de " + selected) ;
		//	sendMessage(target, message); //TODO timeout
			votebanInprogress = false;
			return;
		}
	
	}
	
	
	@Override
	protected void onDisconnect() {
		super.onDisconnect();
		try {
			this.reconnect();
		} catch (IOException | IrcException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	protected void onJoin(String channel, String sender, String login,String hostname) {
		super.onJoin(channel, sender, login, hostname);
		if(login.equalsIgnoreCase(this.getName()))
				System.out.println("bot ready");
		//	sendMessage(channel, "coucou");
	}
}
