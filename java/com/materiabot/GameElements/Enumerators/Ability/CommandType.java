package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum CommandType{
	Normal(1, "Normal"),
	EX(2, "EX"),
	BT(7, "BT"),
	LD(8, "LD?"),
	Unknown(3, "Unknown");

	private int id;
	private String description;
	
	private CommandType(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static CommandType get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}