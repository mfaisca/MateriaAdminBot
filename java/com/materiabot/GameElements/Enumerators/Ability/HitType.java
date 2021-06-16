package com.materiabot.GameElements.Enumerators.Ability;
import java.util.Arrays;

public enum HitType{
	BRV(1), 
	HP(2), 
	Buff(6), 
	Debuff(7), 
	FixedBRV(8),
	BRVIgnoreDEF(9), 
	Shield(10), 
	Heal(11), 
	RandomBRV(12), //(Snowstorm [Blue Dragon])
	BRVIgnoreDEF2(14), 
	SketchSummon(15),
	NoBRVConsumption(16),
	SplitHP(19), //Summon Only
	DistributedHP(21),; //Summon Only
	
	private int id;
	
	private HitType(int id) { this.id = id; }

	public int getId() {
		return id;
	}
	
	public static HitType get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}