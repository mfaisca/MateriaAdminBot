package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum AttackType{ //0 - 49
	None(0, "None"),
	Melee(1, "Melee Attack"),
	Ranged(2, "Ranged Attack"),
	Magic(3, "Magic Attack"),
	Ranged_Melee(4, "Melee + Ranged Attack"),
	Magic_Melee(5, "Melee + Magic Attack"),
	Magic_Ranged(-1, "Ranged + Magic Attack"),
	Buff(-1, "Buff"),
	Debuff(-1, "Debuff"),
	Shield(-1, "Shield"),
	Heal(-1, "Heal");

	private int id;
	private String description;
	
	private AttackType(int id, String desc) {
		this.id = id;
		this.description = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	public static AttackType get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}