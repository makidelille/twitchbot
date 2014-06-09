package lib;

import java.util.HashMap;

public class Monstro99 extends StreamerData{

	public HashMap<String,String> cfg = new HashMap<String, String>();
	public String resolution = "1280x800";
	
	public Monstro99() {
		this.init();
	}
	
	public void init() {
		cfg.put("CPU", "I7 860+@4.2Ghz Corsair H50 + 2x Cooler Master Excalibur");
		cfg.put("RAM", "8Go Crucial Ballistix pc16000 (2000Mhz)");
		cfg.put("GPU", "MSI GTX 770 OC 2Go Gaming");
	}
	
	@Override
	public HashMap<String, String> getCfg() {
		return this.cfg;
	}
	
	@Override
	public String getResolution() {
		return resolution;
	}
	
}
