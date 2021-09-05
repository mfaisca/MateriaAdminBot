package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum MiscConditionTarget {
	Target(1, "Target "),
	Self(2, "Self "),
	Party(3, "Party "),
	Enemy(5, "Any Enemy "),
	Self2(6, "Self "), //??
	NumOfPartyMembers(7, "Number of Party Members "),
	Target2(25, "Target "),
	
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