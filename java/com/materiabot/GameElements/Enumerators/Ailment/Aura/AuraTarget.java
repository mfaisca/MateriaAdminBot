package com.materiabot.GameElements.Enumerators.Ailment.Aura;
import java.util.Arrays;

public enum AuraTarget {
	Target(1, ""),
	Self(2, "Self"),
	Random(3, "Random"),
	Enemies(5, "All Enemies"),
	Party(6, "Party"),
	Allies(7, "Allies"),
	;
	
	private int id;
	private String description;
	
	private AuraTarget(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static AuraTarget get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}