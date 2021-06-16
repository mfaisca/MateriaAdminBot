package com.materiabot.GameElements.Enumerators.Ability.HitData;
import java.util.Arrays;

public enum Type{
	BRV(1), HP(2), Buff(6), Debuff(7), FixedBRV(8), BRVIgnoreDEF(9), Shielding(10), Healing(11), RandomMagicBRV(12), 
	BRVIgnoreDEF2(14), SketchSummon(15), NoBRVConsumption(16), SplitSummonHP(19), DistributedSummonHP(21);
	
	private int id;
	
	private Type(int id) { this.id = id; }

	public int getId() {
		return id;
	}

	public static boolean isBRV(Type t) {
		return t.equals(BRV) || t.equals(FixedBRV) || t.equals(BRVIgnoreDEF) || 
				t.equals(RandomMagicBRV) || t.equals(BRVIgnoreDEF2) || t.equals(SketchSummon) || 
				t.equals(NoBRVConsumption);
	}
	public static boolean isHP(Type t) {
		return t.equals(HP) || t.equals(SplitSummonHP) || t.equals(DistributedSummonHP);
	}
	
	public static Type get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}