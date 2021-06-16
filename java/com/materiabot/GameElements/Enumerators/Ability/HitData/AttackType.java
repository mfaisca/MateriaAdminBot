package com.materiabot.GameElements.Enumerators.Ability.HitData;
import java.util.Arrays;

public enum AttackType{
	None(0),
	Melee(1),
	Ranged(2),
	Magic(3);
	
	private int id;
	
	private AttackType(int id) { this.id = id; }
	
	public int getId() { return id; }
	public static AttackType get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
	public String getEmote() { return "attackType_" + name(); }
}