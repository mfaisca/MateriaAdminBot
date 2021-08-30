package com.materiabot.GameElements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import Shared.Methods;

public class Equipment{	
	public static enum Type{
		Dagger(), Sword(), Greatsword(), Staff(), Gun(), Fist(), ThrowingWeapon(),
		Spear(), Bow(), Whip(), Other(), 
		Armor(), Artifact(), BloomStone();
		
		public String getEmote() { return name() + "Equip"; }
		//public String getTrashEmote() { return "Trash" + name(); }
		
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

		public static Type find(int id) {
			if((id-1) > Type.values().length)
				System.out.println();
			return values()[id-1];
		}
	}
	public static enum Rarity{
		W_3S(3, "bronzeWeapon", "3w", "1cp", "3", "3w", "3*"), 
		W_4S(4, "silverWeapon", "4w", "10cp", "4", "4w", "4*"), 
		W_15(5, "baseWeapon", "15", "15cp", "15", "15cp", "c15", "1", "s1"), 
		W_35(5, "uniqueWeapon", "35", "35cp", "35", "35cp", "c35", "2", "s2"), 
		W_WoI(5, "summonWeapon", "woi", "emptyCrystal", "woi", "summon"), 
		W_NT(5, "ntWeapon", "NT", "ntlogo", "nt"), 
		W_MW(5, "manikinWeapon", "dark", "IronManikin", "manikin", "mw", "dark", "shadow"), 
		W_EX(5, "exWeapon", "ex", "70cp", "ex", "ex3", "ex2", "ex1"), 
		W_EXP(6, "realizedWeapon", "ex+", "ex+", "exp", "ex+3", "ex+2", "ex+1", "ex+0"), 
		W_LD(5, "limitedWeapon", "ld", "ld", "limited", "ld3", "ld2", "ld1"), 
		W_BT(1000, "burstWeapon", "bt", "burst", "bt"),
		W_BTP(1001, "rzBurstWeapon", "bt+", "burst+", "bt+", "bt+0", "bt+1", "bt+2", "bt+3"),
		A_3S(3, "bronzeArmor", "3a", "a1cp", "3a", "a3"), 
		A_4S(4, "silverArmor", "4a", "a10cp", "4a", "a4"), 
		A_35(5, "uniqueArmor", "35a", "armorToken", "35a", "a35"), 
		A_90(5, "exArmor", "hg", "armorTokenP", "90a", "a90"), 
		A_90P(6, "realizedArmor", "hg+", "armorTokenP", "90a+", "130a", "a90+", "a130", "90ap", "a90p"), 
		A_7S(7, "highArmor", "7a", "highArmorToken", "ha", "210a", "a210"),
		A_7SP(8, "rzHighArmor", "7ap", "highArmorBook", "ha+", "230a", "a230"),
		BS(0, "bloomStone", "bloom", "bloom", "bloom", "bs");
		
		private int rarity;
		private String name, imageName, emoteName;
		private List<String> names = new LinkedList<String>();
		
		private Rarity(int rarity, String name, String imageName, String emojiName, String... gearNames) { 
			this.rarity = rarity; this.name = name; this.imageName = imageName; this.emoteName = emojiName;
			names = Arrays.asList(gearNames);
		}

		public int getRarity() { return rarity; }
		public String getName() { return name; }
		public String getImageName() { return imageName; }
		public String getEmoteName() { return emoteName; }
		public String getEmoteNameC() { return emoteName + "Circle"; }
		public String getEmoteNameS() { return emoteName + "Square"; }
		public List<String> getNames() { return names; }

		public static Rarity getByName(String s) {
			for(Rarity r : values())
				if(r.getName().equals(s))
					return r;
			return null;
		}
		public static Rarity getByEmoji(String s) {
			for(Rarity r : values())
				if(r.getEmoteName().equals(s) || r.getEmoteNameC().equals(s) || r.getEmoteNameS().equals(s))
					return r;
			return null;
		}
		public static Rarity getByTags(String s) {
			for(Rarity r : values())
				if(r.getNames().contains(s.toLowerCase()))
					return r;
			return null;
		}

		public static boolean isLowerTier(Rarity r) {
			return Arrays.asList(W_4S, W_15, W_35, W_WoI, W_NT, W_MW).contains(r);
		}
		public static boolean isUpperTier(Rarity r) {
			return Arrays.asList(W_EX, W_EXP, W_LD, W_BT).contains(r);
		}
		public static boolean isWeapon(Rarity r) {
			return isLowerTier(r) || isUpperTier(r);
		}
		public static boolean isArmor(Rarity r) {
			return Arrays.asList(A_4S, A_35, A_90, A_90P, A_7S).contains(r);
		}
	}
	
	private int id;
	private Text name;
	private List<Passive> passives = new ArrayList<Passive>(1);
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