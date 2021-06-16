package com.materiabot.GameElements.Enumerators.Passive;

public enum MonsterType {
	Wolf(4, "Wolf"),
	Skeleton(7, "Skeleton");
	
	private String name;
	private int id;
	
	private MonsterType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() { return id; }
	public String getName() { return name; }
	
	public static MonsterType getById(int id) {
		for(MonsterType mt : values())
			if(mt.getId() == id)
				return mt;
		return null;
	}
}