package com.materiabot.GameElements.Enumerators.Passive;

public enum PassiveTarget {
	T2(2, "Self", ""),
	T3(3, "Party", "party ");
	
	private int id;
	private String name, description;
	
	private PassiveTarget(int id, String name, String description) {
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
	
	public static PassiveTarget get(int id) {
		for(PassiveTarget t : values())
			if(t.getId() == id)
				return t;
		return null;
	}
}