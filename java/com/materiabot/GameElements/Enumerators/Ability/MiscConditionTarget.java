package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum MiscConditionTarget {
	Target(1, "target"),
	Self(2, "self"),
	Party(3, "party"),
	Enemy(5, "any enemy"),
	Party2(6, "party"), //Used MiscCondition 39 at least
	Party3(7, "party"),
	Target2(25, "target"),
	
	;private int id;
	private String description;
	
	private MiscConditionTarget(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static MiscConditionTarget get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}