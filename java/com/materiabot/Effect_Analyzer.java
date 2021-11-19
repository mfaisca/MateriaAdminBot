package com.materiabot;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.ChainAbility;
import com.materiabot.GameElements.MiscCondition;
import com.materiabot.GameElements.Region;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ability.ChargeRate;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Effect._AbilityEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect._AuraEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.GameElements.Enumerators.Passive.Effect._PassiveEffect;
import com.materiabot.GameElements.Enumerators.Passive.Required._PassiveRequired;
import com.materiabot.IO.JSON.UnitParser;
import com.materiabot.Utils.Constants;
import Shared.Dual;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		UnitParser.setDebug(true);
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		int key = 9;

		if(key == 1)
			printUnit("Ace", AttackName.LD);
		if(key == 2)
			printUnitSpecific("The Emperor", 6058);
		if(key == 3)
			findMissing();
		if(key == 4 || key == 99)
			printAllAilments();
		if(key == 5 || key == 99)
			printAllAuras();
		if(key == 6 || key == 99)
			findLabels();
		if(key == 7 || key == 99)
			printAllAbilityEffects();
		if(key == 8 || key == 99)
			printAllPassives();
		if(key == 9)
			selphieCalculation();
	}

	private static void printUnit(String unit, AttackName atn) {
		//Arrays.asList(
				_Library.L.getUnit(unit).getJP().getAbility(atn).stream()
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
		HashMap<Integer, List<String>> map = new HashMap<>();
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
				.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json")))
				.map(u -> _Library.L.getUnit(u))
				.flatMap(u -> u.getAbilities().values().stream())
				.flatMap(a -> a.getAilments().stream())
				.filter(a -> a.getAilmentConditionId() > 0)
				.forEach(a -> {
					if(map.get(a.getAilmentConditionId()) == null)
						map.put(a.getAilmentConditionId(), new LinkedList<>());
					map.get(a.getAilmentConditionId()).add(a.getUnit().getName() + "/" + a.getAbility().getName().getBest() + "(" + a.getAbility().getId() + ")/" + a.getName().getBest() + "(" + a.getId() + ")");
				});
//		map.keySet().stream().sorted().forEach(k -> {
//			System.out.println(k + " - " + map.get(k).stream().distinct().collect(Collectors.toList()).toString());
//		});
		map.entrySet().stream().forEach(es -> {
			System.out.println(es.getKey() + ": " + es.getValue().toString());
		});
	}
	
	private static void printAllAbilityEffects() {
		HashMap<Integer, List<Unit>> map = new HashMap<>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> u.getAbilities().values().stream())
			.flatMap(a -> a.getHitData().stream())
			.distinct()
			.forEach(aid -> {
				_AbilityEffect a = Constants.ABILITY_EFFECT.get(aid.getEffectId());
				if(a == null) {
					if(!map.containsKey(aid.getEffectId()))
						map.put(aid.getEffectId(), new LinkedList<>());
					map.get(aid.getEffectId()).add(aid.getAbility().getUnit());
				}
			});
		System.out.println("ABILITY EFFECTS:");
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k).toString());
		});
	}
	
	private static void printAllPassives() {
		HashMap<Integer, List<Unit>> map = new HashMap<>();
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> u.getPassives().values().stream())
			.flatMap(a -> a.getEffects().stream())
			.distinct()
			.forEach(aid -> {
				_PassiveEffect a = Constants.PASSIVE_EFFECT.get(aid.getEffectId());
				if(a == null) {
					if(!map.containsKey(aid.getEffectId()))
						map.put(aid.getEffectId(), new LinkedList<>());
					map.get(aid.getEffectId()).add(aid.getPassive().getUnit());
				}
			});
		System.out.println("PASSIVE EFFECTS:");
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k).toString());
		});
		HashMap<Integer, List<Unit>> map2 = new HashMap<>();
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> u.getPassives().values().stream())
			.flatMap(a -> a.getConditions().stream())
			.distinct()
			.forEach(aid -> {
				_PassiveRequired a = Constants.PASSIVE_REQUIRED.get(aid.getRequiredId());
				if(a == null) {
					if(!map2.containsKey(aid.getRequiredId()))
						map2.put(aid.getRequiredId(), new LinkedList<>());
					map2.get(aid.getRequiredId()).add(aid.getPassive().getUnit());
				}
			});
		System.out.println("PASSIVE REQUIREDS:");
		map2.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map2.get(k).toString());
		});
	}
	
	private static class IDUnitContainer{
		public int id;
		public Unit u;
		
		public IDUnitContainer(int id, Unit u) {this.id = id; this.u = u; }
	}
	
	private static void printAllAilments() {
		HashMap<Integer, List<Unit>> map = new HashMap<>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> 
				Streams.concat(
						u.getAilments().values().stream().flatMap(a -> Arrays.asList(a.getEffects()).stream().map(ae -> new IDUnitContainer(ae, u))),
						u.getAilments().values().stream().flatMap(a -> a.getAuras().stream()).map(a -> new IDUnitContainer(a.getEffectId(), u))
				)
			)
			.sorted((a1, a2) -> Integer.compare(a1.id, a2.id))
			.filter(idu -> idu.id != 69)
			.forEach(idu -> {
				int aid = idu.id;
				_AilmentEffect a = Constants.AILMENT_EFFECT.get(aid);
				if(a == null){
					if(!map.containsKey(aid))
						map.put(aid, new LinkedList<>());
					if(!map.get(aid).contains(idu.u))
						map.get(aid).add(idu.u);
				}
			});
		System.out.println("AILMENT EFFECTS:");
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k));
		});
		HashMap<Integer, List<Unit>> map2 = new HashMap<>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> 
				Streams.concat(
						u.getAilments().values().stream().flatMap(a -> Arrays.asList(a.getConditions()).stream())
				)
			)
			.flatMap(aid -> aid.getConditions().stream())
			.distinct()
			.forEach(aid -> {
				if(aid.getCondition() == null){
					if(!map2.containsKey(aid.getConditionId()))
						map2.put(aid.getConditionId(), new LinkedList<>());
					map2.get(aid.getConditionId()).add(aid.getAilment().getUnit());
				}
			});
		System.out.println("AILMENT CONDITIONS:");
		map2.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map2.get(k).toString());
		});
	}
	
	private static void printAllAuras() {
		HashMap<Integer, List<Unit>> map = new HashMap<>();
		
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> 
				Streams.concat(
						u.getAilments().values().stream().flatMap(a -> a.getAuras().stream())
				)
			)
			.distinct()
			.filter(aid -> aid.getTypeId() != -1)
			.forEach(aid -> {
				_AuraEffect a = Constants.AURA_EFFECT.get(aid.getTypeId());
				if(a == null) {
					if(!map.containsKey(aid.getTypeId()))
						map.put(aid.getTypeId(), new LinkedList<>());
					map.get(aid.getTypeId()).add(aid.getAilment().getUnit());
				}
			});
		System.out.println("AURA EFFECTS:");
		map.keySet().stream().distinct().sorted().forEach(k -> {
			System.out.println(k + " - " + map.get(k).toString());
		});
	}
	
	private static void findLabels() {
		HashMap<Integer, List<String>> map = new HashMap<>();
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
			.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).map(u -> _Library.L.getUnit(u))
			.flatMap(u -> Streams.concat(u.getUpgradedAbilities().stream(), u.getTriggeredAbilities().stream()))
			.forEach(hd -> {
				Ability oga = hd.getUnit().getSpecificAbility(hd.getOriginalId());
				String ogaS = (oga == null ? "Unknown Ability(" : (oga.getName().getBest() + "(")) + oga.getId() + ")";
				Ability tga = hd.getUnit().getSpecificAbility(hd.getSecondaryId());
				String tgaS = (tga == null ? "Unknown Ability(" : (tga.getName().getBest() + "(")) + tga.getId() + ")";
				for(MiscCondition mc : hd.getReqMiscConditions()) {
					if(mc.getLabel() == null) {
						if(!map.containsKey(mc.getLabelId()))
							map.put(mc.getLabelId(), new LinkedList<>());
						String tn = (mc.getTarget() == null ? "" : mc.getTarget().getDescription()) + "(" + mc.getTargetId() + ")";
						map.get(mc.getLabelId()).add(hd.getUnit().getName() + " -- " + ogaS +"->" + tgaS + "T:" + tn + " -- " + Arrays.toString(mc.getValues()));
					}	
				}
			});
		for(Entry<Integer, List<String>> k : map.entrySet()) {
			System.out.println("Label:" + k.getKey());
			for(String s : k.getValue().stream().distinct().collect(Collectors.toList()))
				System.out.println("\t" + s);
		}
	}
	
//	private static void biggestChain() {
//		Dual<Integer, List<String>> results = new Dual<>(null, null);
//		Streams.concat(	Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units\\gl").list()).stream(),
//						Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units\\jp").list()).stream())
//		.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json"))).distinct()
//		.map(u -> _Library.L.getUnit(u))
//		.filter(u -> u != null)
//		.forEach(u -> {
//			Map<Integer, List<Integer>> trig = new HashMap<>();
//			for(ChainAbility ca : u.getTriggeredAbilities()) {
//				if(!trig.containsKey(ca.getOriginalId()))
//					trig.put(ca.getOriginalId(), new LinkedList<>());
//				trig.get(ca.getOriginalId()).add(ca.getSecondaryId());
//			}
//			
//		});
//	}
	
	private static void selphieCalculation() {
		HashMap<Integer, List<String>> map = new HashMap<>();
		Arrays.asList(new File("E:\\WorkspaceV3\\_Launcher\\resources\\units").list()).stream()
				.map(u -> u.substring(u.indexOf("_")+1, u.indexOf(".json")))
				.map(u -> _Library.L.getUnit(u))
				.map(u -> u.get(Region.JP))
				.filter(u -> u != null)
				.map(u -> u.getAbility(AttackName.EX).get(0))
				.forEach(a -> {
					ChargeRate b = ChargeRate.getBy(a.getChargeRate());
					ChargeRate u = ChargeRate.getBy((int)(a.getChargeRate() * 0.8));
					if(b != u)
						System.out.println(a.getUnit().getCommon().getName() + "||" + b.getDescription().getBest() + " -> " + u.getDescription().getBest());
				});
		map.entrySet().stream().forEach(es -> {
			System.out.println(es.getKey() + ": " + es.getValue().toString());
		});
	}
}