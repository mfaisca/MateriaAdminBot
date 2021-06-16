package com.materiabot.GameElements.Enumerators.Passive;

public enum EffectTarget {
	T2(2, "Self", ""),
	T3(3, "Party", "party "),
	T4(5, "Allies", "allies "),
	T11(8, "Caller", "caller "),;
	
	private int id;
	private String name, description;
	
	private EffectTarget(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	
	public static EffectTarget get(int id) {
		for(EffectTarget t : values())
			if(t.getId() == id)
				return t;
		return null;
	}
}