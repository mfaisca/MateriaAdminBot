package com.materiabot.GameElements.Enumerators.Ability;

import java.util.Arrays;

public enum TargetRange {
	Target(1, "Target");

	private int id;
	private String description;
	
	private TargetRange(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static TargetRange get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}