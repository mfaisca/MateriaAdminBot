package com.materiabot;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Unit;
import com.materiabot.Utils.Constants;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		PluginManager.loadCommands();
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		
		unitEval("pecil");
		
//		String[] v = {"10020020"};
//		int[][] ret = Methods.splitRankData(v);
//		System.out.println(ret[0][0]);
//		System.out.println(ret[0][1]);
//		System.out.println(ret[0][2]);
	}
	
	private static void unitEval(String unit) throws Exception {
		Unit u = _Library.L.getUnit(unit);
		int id = 11250;
		System.out.println(u.getSpecificAbility(id).generateDescription() + "\n\n");
		for(Ailment a : u.getSpecificAbility(id).getAilments())
			System.out.println(a.generateDescription() + System.lineSeparator());
		Constants.sleep(1000);
	}
}