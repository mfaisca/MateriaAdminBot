package com.materiabot.GameElements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.materiabot.GameElements.Summon._Summon;
import Shared.Methods;

public class Equipment{	
	public enum Type{
		Dagger(), Sword(), Greatsword(), Staff(), Gun(), Fist(), ThrowingWeapon(),
		Spear(), Bow(), Whip(), Other(), 
		Armor(), Artifact(), BloomStone();
		
		public String getEmote() { return name() + "Equip"; }
		
		public static Type random3Star() {
			int rng = Methods.RNG.nextInt(8);
			if(rng == 7) rng = 11;
			return values()[rng];
		}
		public static Type random4Star() {
			int rng = Methods.RNG.nextInt(8);
			if(rng == 7) rng = 11;
			return values()[rng];
		}
		public static Type random5Star() {
			int rng = Methods.RNG.nextInt(11);
			return values()[rng];
		}
		public boolean isWeapon() {
			return this.ordinal() <= Other.ordinal();
		}

		public static Type find(int id) {
			return values()[id-1];
		}
	}
	public enum Rarity{
		W_3S(3, "bronzeWeapon", "E_3star"), 
		W_4S(4, "silverWeapon", "E_4star"), 
		W_15(5, "baseWeapon", "W_15cp"), 
		W_35(5, "uniqueWeapon", "W_35cp"), 
		W_WoI(5, "summonWeapon", "W_WoI"), 
		W_NT(5, "ntWeapon", "W_NT"), 
		W_MW(5, "manikinWeapon", "W_MW"), 
		W_EX(5, "exWeapon", "W_EX"), 
		W_EXP(6, "realizedWeapon", "W_EXP"), 
		W_LD(5, "limitedWeapon", "W_LD"), 
		W_BT(1000, "burstWeapon", "W_BT"),
		W_BTP(1001, "rzBurstWeapon", "W_BTP"),
		A_3S(3, "bronzeArmor", "E_3star"), 
		A_4S(4, "silverArmor", "E_4star"), 
		A_35(5, "uniqueArmor", "A_35"), 
		A_90(5, "exArmor", "A_90"), 
		A_90P(6, "realizedArmor", "A_90P"), 
		A_7S(7, "highArmor", "A_7S"),
		A_7SP(8, "rzHighArmor", "A_7SP"),
		BS(0, "bloomStone", "E_BS");
		
		private int rarity;
		private String name, emoteName;
		
		private Rarity(int rarity, String name, String emoteName) { 
			this.rarity = rarity; this.name = name; this.emoteName = emoteName;
		}

		public int getRarity() { return rarity; }
		public String getName() { return name; }
		public String getEmoteName() { return emoteName; }
		public String getEmoteName(Unit u) { return _Summon.getSummonFromWoIWeapon(u).getEmoteCrystal(); }
//		public String getEmoteNameC() { return emoteName + "Circle"; }
//		public String getEmoteNameS() { return emoteName + "Square"; }

		public static Rarity getByName(String s) {
			for(Rarity r : values())
				if(r.getName().equals(s))
					return r;
			return null;
		}
		public static Rarity getByEmoji(String s) {
			for(Rarity r : values())
//				if(r.getEmoteName().equals(s)) || r.getEmoteNameC().equals(s) || r.getEmoteNameS().equals(s))
					return r;
			return null;
		}

		public static boolean isLowerTierW(Rarity r) {
			return Arrays.asList(W_15, W_35, W_WoI, W_NT, W_MW).contains(r);
		}
		public static boolean isUpperTierW(Rarity r) {
			return Arrays.asList(W_EX, W_EXP, W_LD, W_BT).contains(r);
		}
		public static boolean isLowerTierA(Rarity r) {
			return Arrays.asList(A_35, A_90, A_90P, BS).contains(r);
		}
		public static boolean isUpperTierA(Rarity r) {
			return Arrays.asList(A_7S, A_7SP).contains(r);
		}
		public static boolean isWeapon(Rarity r) {
			return isLowerTierW(r) || isUpperTierW(r);
		}
		public static boolean isArmor(Rarity r) {
			return isLowerTierA(r) || isUpperTierA(r);
		}
	}
	
	private int id;
	private Text name;
	private List<Passive> passives = new ArrayList<>(1);
	private Type type;
	private Rarity rarity;
	private Unit unit;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Text getName() { return name; }
	public void setName(Text name) { this.name = name; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
	public List<Passive> getPassives() { return passives; }
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	public Rarity getRarity() { return rarity; }
	public void setRarity(Rarity rarity) { this.rarity = rarity; }
}