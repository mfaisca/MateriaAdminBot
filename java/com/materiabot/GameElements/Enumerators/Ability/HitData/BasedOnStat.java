package com.materiabot.GameElements.Enumerators.Ability.HitData;
import java.util.Arrays;

public enum BasedOnStat{
	Stat1(1, "???"),
	Stat2(2, "Attack"),
	Stat3(3, "Initial BRV"),
	Stat4(4, "Max BRV"),
	Stat5(5, "Current BRV"),
	Stat6(6, "Max HP"),
	Stat7(7, "Current HP"),
	Stat8(8, "Initial BRV"),
	Stat9(9, "Max BRV"),
	Stat10(10, "Current BRV"),
	Stat11(11, "Max HP"),
	Stat12(12, "Target Current BRV"), //Locke unused skills?
	Stat13(13, "BRV Damage Dealt"),
	Stat14(14, "HP Damage Dealt"),
	Stat15(15, "Attack"),
	Stat16(16, "Attack"),
	Stat21(21, "highest current BRV"),
	Stat22(22, "Attack"),
	Stat29(29, "Unknown"), //Jecht unused skill?
	Stat36(36, "HP Damage Dealt"), //Serah EX only?
	Stat37(37, "total Current HP"), //Unique to Ignis?
	Stat41(41, "gravity damage dealt"), //Unique to Zidane Mug?
	Stat42(42, "HP Damage Dealt"),
	Stat46(46, "Attack at random"), //Wakka EX only, Random between 4 values on arguments
	Stat49(49, "Attack"),
	Stat1014(1014, "HP Damage Dealt (based on the number of 「**Note**」 on the party)"),
	;
	
	private int id;
	private String stat;
	
	private BasedOnStat(int id, String stat) { this.id = id; this.stat = stat; }

	public int getId() { return id; }
	public String getStat() { return stat; }

	public static BasedOnStat get(int id) {
		return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}