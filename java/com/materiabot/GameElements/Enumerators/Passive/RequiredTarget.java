package com.materiabot.GameElements.Enumerators.Passive;

public enum RequiredTarget {
	T1(1, "Target", "target "),
	T2(2, "Self", ""),
	T3(3, "Party", "party "),
	T5(5, "All Enemies", "all enemies "),
	T8(8, "Target", "target "),
	T10(10, "Targeted Enemy", "targeted enemy "),
	T11(11, "Call", "call ability"),
	T14(14, "Debuffed Target", "target "),;
	
	private int id;
	private String name, description;
	
	private RequiredTarget(int id, String name, String description) {
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
	
	public static RequiredTarget get(int id) {
		for(RequiredTarget t : values())
			if(t.getId() == id)
				return t;
		return null;
	}
}