package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Ifrit30 extends _Summon{
	protected Ifrit30() {
		super(Arrays.asList("Ifrit"), 30, Element.Fire, "Hellfire", 
				"Raises Attack by 30% when HP > 50%", 
					"Fire BRV Magic damage" + System.lineSeparator() + 
					"Grants Fire attribute to the party", 
				"10|8|6|14|12", "Vanille|Snow|Cater|Terra|Celes|Balthier", 6, 6000, "Moderately Fast", 
				new SummonPassive("Fire Resist Up", "Lowers Fire damage taken by 10%.", "Fire dmg taken -10%"),
				new SummonPassive("Fire Resist All", "Lowers Party Fire damage taken by 3%.", "Fire party dmg taken -3%"), 
				new SummonPassive("Fire Power Up", "Raises Fire BRV damage dealt by 10%.", "Fire dmg dealt +10%"),
				new SummonPassive("Ifrit Attack Up", "Raises Attack by 10% when HP > 80%.", "ATK+10% if HP>80%", true), 
				new SummonPassive("Ifrit Base&Attack Up", "Raises Initial BRV by 10% and Attack by 2% when HP > 80%.", "IBrv+10% & ATK+2% if HP>80%", true), 
				new SummonPassive("Ifrit Bonus Up", "Raises Break Bonus by 5% when HP > 80%.", "Break Bonus +5% if HP>80%"), 
				new SummonPassive("Ifrit Critical Power Up", "Raises Critical BRV damage by 10% when HP > 80%.", "Crit dmg dealt +10% if HP>80%", true));
	}
}