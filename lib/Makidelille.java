package lib;

import java.util.HashMap;

public class Makidelille extends StreamerData{

	public static HashMap<String,String> cfg;
	public static String resolution = "TODO";
	
	public void init() {
		cfg = new HashMap<String, String>();
		cfg.put("CPU", "I5 3570 @4.1GHz");
		cfg.put("RAM", "ram data");
		cfg.put("GPU", "gpu data");
		System.out.println("has init");
		
	}

}
