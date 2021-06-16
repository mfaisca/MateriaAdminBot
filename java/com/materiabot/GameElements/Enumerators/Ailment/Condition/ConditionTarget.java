package com.materiabot.GameElements.Enumerators.Ailment.Condition;
import java.util.Arrays;

public enum ConditionTarget {
	Target(1, "Target"),
	Self(2, "Self"),
	Party(3, "Party"),
	Enemies(5, "All Enemies"),
	Target8(8, "Target"),
	Target9(9, "Afflected Target"),
	Target14(14, "Target with ailment"),
	;
	
	private int id;
	private String description;
	
	private ConditionTarget(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static ConditionTarget get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}