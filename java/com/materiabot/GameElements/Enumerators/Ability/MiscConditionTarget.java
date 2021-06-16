package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum MiscConditionTarget {  //XXX FIGURE THESE OUT OR ASK REM
	Self(2, "Self"),
	
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