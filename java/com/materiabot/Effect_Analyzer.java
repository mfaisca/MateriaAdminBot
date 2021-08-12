package com.materiabot;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.Utils.Constants;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		//PluginManager.loadCommands();
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		int key = 1;
		
		if(key == 1)
			printUnit("Selphie", AttackName.S2);
		if(key == 2)
			printUnitSpecific("Papalymo", 15537);
		if(key == 3)
			findMissing();
	}

	private static void printUnit(String unit, AttackName atn) {
		//Arrays.asList(
				_Library.L.getUnit(unit).getAbility(atn).stream()
		//		.reduce((a1, a2) -> new Ability.MultiAbility(a1, a2)).orElse(null))
		.forEach(p -> {
			String text = p.generateDescription();
			if(text.length() > 0) {
				System.out.println("------------------------------");
				System.out.println("------------------------------");
				System.out.println("------------------------------");
				System.out.println("------------------------------");
				text = p.generateTitle() + System.lineSeparator() + System.lineSeparator() + p.generateDescription();
				System.out.println(text);
			}
			for(Ailment a : p.getAilments()) {
				System.out.println("------------------------------");
				String desc = a.generateDescription();
				if(desc.length() > 0)
					System.out.println(a.generateTitle() + a.generateDescription());
			}
		});
	}
	private static void printUnitSpecific(String unit, Integer... id) {
		//Arrays.asList(
		Arrays.asList(id).stream().map(i -> _Library.L.getUnit(unit).getSpecificAbility(i))
		//	.reduce((a1, a2) -> new Ability.MultiAbility(a1, a2)).orElse(null))
		.forEach(p -> {
			String text = p.generateDescription();
			if(text.length() > 0) {
				text = p.getName().getBest() + " (ID: " + p.getId() + ")" + System.lineSeparator() + System.lineSeparator() + p.generateDescription();
				System.out.println(text);
			}
			for(Ailment a : p.getAilments()) {
				System.out.println("------------------------------");
				String desc = a.generateDescription();
				if(desc.length() > 0)
					System.out.println(a.generateTitle() + a.generateDescription());
			}
		});
	}
	
	private static void findMissing() {
		HashMap<Integer, List<Unit>> map = new HashMap<Integer, List<Unit>>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> u.getAilments().values().stream())
			.forEach(hd -> {
				for(Integer i : hd.getEffects()) {
					if(i == -1) continue;
					if(Constants.AILMENT_EFFECT.get(i) != null && Constants.AILMENT_EFFECT.get(i).getBaseDescription().length() > 0) continue;
					if(!map.containsKey(i))
						map.put(i, new LinkedList<Unit>());
					if(!map.get(i).contains(hd.getUnit()))
						map.get(i).add(hd.getUnit());
				}
			});
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k).toString());
		});
	}
}