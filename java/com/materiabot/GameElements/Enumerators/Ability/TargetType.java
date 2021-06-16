package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum TargetType{
	Counter(1, "Counter"),
	Enemy(2, "Enemy"),
	Ally(3, "Ally"),
	OtherAlly(4, "Other Ally"),
	Self(5, "Self"),
	Party(7, "Party");

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