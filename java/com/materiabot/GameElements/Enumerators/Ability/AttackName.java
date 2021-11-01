package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum AttackName{
	BRV("brv", "brv+", "brv++", "brv+++", "brv++++"), HP("hp", "hp+", "hp++", "hp+++", "hp++++"), 
	S1("s1", "1"), S2("s2", "2"), AA("aa", "additional"), EX("ex", "ex+", "ex++"), 
	LD("ld", "limited"), BT("bt", "burst", "bt+", "burst+"), FR("fr", "force"), 
	CA("ca", "call"), CALD("cald", "ldcall", "ldca", "callld");
	private List<String> names = new LinkedList<>();
	
	private AttackName(String... skillNames) { 
		names = Arrays.asList(skillNames);
	}
	
	public List<String> getNames(){ return names; }
	public String getEmoteName() { return "S_" + name(); }
	
	public static AttackName getByTags(String s) {
		for(AttackName t : values())
			if(t.names.contains(s.replace("\\+", "").toLowerCase()))
				return t;
		return null;
	}
}