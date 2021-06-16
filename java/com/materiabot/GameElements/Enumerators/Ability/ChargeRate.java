package com.materiabot.GameElements.Enumerators.Ability;
import com.materiabot.GameElements.Text;

public enum ChargeRate{
	crEFast(3000, "Fast+++", "Extremely Fast"),
	crVFast(6000, "Fast++", "Very Fast"),
	crFast(9000, "Fast+", "Fast"),
	crFast2(10350, "Fast+", "Fast"),
	crSFast(12000, "Fast", "Slightly Fast"),
	crNormal(14400, "Normal", "Normal"),
	crNormal2(15000, "Normal", "Normal"),
	crSSlow(18000, "Slow", "Slightly Slow"),
	crSlow(21000, "Slow-", "Slow"),
	crVSlow(24000, "Slow--", "Very Slow"),
	
	;private int chargeRate;
	private Text desc;
	
	private ChargeRate(int cr, String g, String j) { 
		desc = new Text(); 
		desc.setGl(g); desc.setJp(j); 
		chargeRate = cr; 
	}

	public int getChargeRate() { return chargeRate; }
	public Text getDescription() { return desc; }
	public static ChargeRate getBy(int chargeRate) {
		for(ChargeRate cr : values()) {
			if(chargeRate <= cr.chargeRate)
				return cr;
		}
		return null;
	}
}