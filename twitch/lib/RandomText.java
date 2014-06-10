package twitch.lib;

import java.util.ArrayList;
import java.util.Random;

public class RandomText {

	private static ArrayList<String> join;
	private static ArrayList<String> leave;
	private static ArrayList<String> ran;
	
	public static void init(){
		
		//TODO load file
		
	}
	
	public static String getRanWelcome(){
		Random rand = new Random();
		int nb =rand.nextInt(join.size()) -1;
		try{
			return join.get(nb);
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getRanBye(){
		Random rand = new Random();
		int nb =rand.nextInt(leave.size()) -1;
		try{
			return leave.get(nb);
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getRanSentence(){
		Random rand = new Random();
		int nb =rand.nextInt(ran.size()) -1;
		try{
			return ran.get(nb);
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			return null;
		}
	}
	
}
