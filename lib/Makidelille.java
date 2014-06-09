package lib;

import java.util.HashMap;

public class Makidelille extends StreamerData{

	public Makidelille() {
		super("makidelille");
	}

	public static HashMap<String,String> cfg;
	public static String resolution = "TODO";
	
	public void init() {
		cfg = new HashMap<String, String>();
		cfg.put("CPU", "I5 3570 @4.1GHz");
		cfg.put("RAM", "ram data");
		cfg.put("GPU", "gpu data");
	}

	@Override
	public HashMap<String, String> getCfg() {
		return cfg;
	}
	
	@Override
	public String getResolution() {
		return resolution;
	}
	
	@Override
	public HashMap<String, String> getControllers() {
		return null;
	}
}
