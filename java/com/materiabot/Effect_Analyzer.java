package com.materiabot;
import Shared.Methods;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.IO.JSON.JSONParser;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.IO.JSON.UnitParser;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		//skillEffectAnalyzer();
		//passiveEffectAnalyzer();
		List<String> l = new LinkedList<String>();
		PluginManager.loadUnits();
		for(Unit u : UnitParser.UNITS) {
			u = _Library.L.getUnit(u.getName());
			for(int i : u.getPassives().keySet())
				if(u.getPassive(i).getId() != u.getJPPassives().get(i).getId())
					l.add(u.getName() + " has different CL" + i + " passives");
		}
		l.stream().forEach(System.out::println);
	}

	private static void skillEffectAnalyzer() {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		File folder = new File("./resources/units/");
		for(File f : folder.listFiles()) {
			String uName = f.getName().substring(f.getName().indexOf("_")+1, f.getName().indexOf("."));
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);
			for(MyJSONObject p : obj.getObjectArray("completeListOfAbilities")) {
				Integer id = p.getInt("id");
				if(id == null) continue;
				String pName = Methods.getBestText(p.getStringArray(p.getObject("name")));
				for(MyJSONObject e : p.getObjectArray("hit_data")) {
					int eId = e.getInt("effect");
					Ability.Details.Hit_Data.EffectType ae = Ability.Details.Hit_Data.EffectType.get(eId);
					if(ae == null)
						store(map, 
								eId + "(" + e.getInt("effect_value_type") + ")", 
								uName + " | " + pName + "(" + id + ") | Type:" + e.getInt("type") + " | " + 
								"Vals:(" + Arrays.toString(e.getIntArray("arguments")) + ")");
				}
			}
		}
		for(String key : map.keySet().stream().sorted().collect(Collectors.toList())) {
			System.out.println(key + ":");
			for(String v : map.get(key))
				System.out.println("\t" + v);
		}
	}
	private static void passiveEffectAnalyzer() {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		File folder = new File("./resources/units/");
		for(File f : folder.listFiles()) {
			String uName = f.getName().substring(f.getName().indexOf("_")+1, f.getName().indexOf("."));
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);
			for(MyJSONObject p : obj.getObjectArray("awakeningPassives")) {
				int pId = p.getInt("id");
				String pName = Methods.getBestText(p.getStringArray(p.getObject("name")));
				for(MyJSONObject e : p.getObjectArray("effects")) {
					int eId = e.getInt("effect_id");
					Passive.Effect pe = Passive.Effect.get(eId);
					int rId = e.getInt("required_id");
					Passive.Required pr = Passive.Required.get(rId);
					if(pe == null)
						store(map, "E"+eId, uName + " | " + pName + "(" + pId + ") | Vals:(" + Arrays.toString(e.getIntArray("effect_values")) + ")");
					if(pr == null)
						store(map, "R"+rId, uName + " | " + pName + "(" + pId + ") | Vals:(" + Arrays.toString(e.getIntArray("required_values")) + ") | Target: " + e.getInt("required_target") + "/" + e.getInt("required_target_value"));
				}
			}
		}
		for(String key : map.keySet().stream().sorted().collect(Collectors.toList())) {
			System.out.println(key + ":");
			for(String v : map.get(key))
				System.out.println("\t" + v);
		}
	}
	
	private static void store(HashMap<String, List<String>> map, String id, String text) {
		if(!map.containsKey(id))
			map.put(id, new LinkedList<String>());
		map.get(id).add(text);
	}
}