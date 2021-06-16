package com.materiabot.GameElements.Enumerators.Ailment;
import java.util.Arrays;

public enum TargetType{
	Target(1, "Target"),
	Self(2, "Self"),
	Allies(4, "Allies"),
	AllEnemies(5, "All enemies"),
	Party(6, "Party"),
	Allies2(7, "Allies"),
	CallUnit(11, "Called Unit"),
	SelfAndTarget(13, "Self & Target"),
	Allies3(15, "Allies"),
	CallerAllies(27, "Allies"),
	CallerTarget(28, "Target"),
	Caller(29, "Caller"),
	;

	private int id;
	private String description;
	
	private TargetType(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static TargetType get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}