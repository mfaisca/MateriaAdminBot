package com.materiabot.GameElements.Enumerators.Ability.HitData;
import java.util.Arrays;

public enum Target{
	ST(1, "target"),
	Self(2, "self"),
	Random(3, "random targets"), //Kuja/Lenna only
	AoE(5, 21, "all enemies"),
	Party(6, 8, "party"),
	Allies(7, "allies"),
	Splash(10, "splash"),
	Alternating(11, 18, "split between enemies"),
	Ally(13, "ally"),
	Traps(18, "traps???"), //Emperor only(S2 / EX)
	Terra(29, "Terra"),
	AOE(23, "traps???"), //Emperor only
	Machina(28, "Machina"),
	Caller(29, "caller"),
	
	;private int id, id2;
	private String desc;

	private Target(int id, String desc) {this.id = id; this.desc = desc; }
	private Target(int id, int id2, String desc) {this.id = id; this.id2 = id2; this.desc = desc; }

	public int getId() {
		return id;
	}
	public int getId2() {
		return id2;
	}
	public String getDescription() {
		return desc;
	}
	public static Target get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id || t.getId2() == id).findFirst().orElse(null);
	}
}