package com.materiabot;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect._AuraEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.Utils.Constants;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		//PluginManager.loadCommands();
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		int key = 1;
		
		if(key == 1)
			printUnit("Rosa", AttackName.LD);
		if(key == 2)
			printUnitSpecific("Exdeath", 13583);
		if(key == 3)
			findMissing();
		if(key == 4)
			printAllAilments();
		if(key == 5)
			printAllAuras();
	}

	private static void printUnit(String unit, AttackName atn) {
		//Arrays.asList(
				_Library.L.getUnit(unit).getAbility(atn, "JP").stream()
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
			.flatMap(a -> a.getAbilities().values().stream())
			.flatMap(a -> a.getAilments().stream())
			.flatMap(a -> a.getAuras().stream())
			.forEach(hd -> {
				Integer[] ii = hd.getRequiredConditionsIds();
				for(Integer i : ii)
					if(i != null && i.intValue() != -1) {
						if(!(Constants.AURA_REQUIRED.get(i) != null && Constants.AURA_REQUIRED.get(i).getBaseDescription().length() > 0)){
							if(!map.containsKey(i))
								map.put(i, new LinkedList<Unit>());
							if(!map.get(i).contains(hd.getAilment().getUnit()))
								map.get(i).add(hd.getAilment().getUnit());
						}
					}
			});
		map.keySet().stream().distinct().sorted().forEach(k -> {
//			boolean ok = Constants.PASSIVE_REQUIRED.get(k) != null && Constants.PASSIVE_REQUIRED.get(k).getBaseDescription().length() > 0;
//			System.out.println(k + " - " + (ok ? "OK" : "NO") + " - " + map.get(k).toString());
			System.out.println(k + " - " + map.get(k).toString());
		});
	}
	
	private static void printAllAilments() {
		HashMap<Integer, List<Unit>> map = new HashMap<Integer, List<Unit>>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> 
				Streams.concat(
						u.getAilments().values().stream().flatMap(a -> Arrays.asList(a.getEffects()).stream()),
						u.getAilments().values().stream().flatMap(a -> a.getAuras().stream()).map(a -> a.getEffectId())
				)
			)
			.distinct()
			.sorted()
			.forEach(aid -> {
				_AilmentEffect a = Constants.AILMENT_EFFECT.get(aid);
				if(a == null)
					System.out.println(aid + " - Effect not parsed!!!!!");
				else
					System.out.println(aid + " - " + a.getBaseDescription());
			});
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k).toString());
		});
	}
	
	private static void printAllAuras() {
		HashMap<Integer, List<Unit>> map = new HashMap<Integer, List<Unit>>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> 
				Streams.concat(
						u.getAilments().values().stream().flatMap(a -> a.getAuras().stream()).map(a -> a.getTypeId())
				)
			)
			.distinct()
			.sorted()
			.forEach(aid -> {
				_AuraEffect a = Constants.AURA_EFFECT.get(aid);
				if(a == null)
					System.out.println(aid + " - Effect not parsed!!!!!");
				else
					System.out.println(aid + " - " + a.getBaseDescription());
			});
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k).toString());
		});
	}
}