package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Odin30 extends _Summon{
	protected Odin30() {
		super(Arrays.asList("Odin"), 30, Element.Null, "Zantetsuken", 
				"Decreases DEF of all enemies by 15%" + System.lineSeparator() + "+30% ATK after hitting HP < 80% once", 
					"Non-elemental BRV Physical damage with 50% BREAK chance" + System.lineSeparator() + 
					"Raises Attack by 30%", 
				"8|6|10|12|14", "Noctis|Cecil Paladin|Fang|Irvine|Seven|Fran", 6, 6000, "Moderately Fast", 
				new SummonPassive("Physical Resist Up", "Lowers Physical damage taken by 10%.", "Physical dmg taken -10%"),
				new SummonPassive("Physical Resist All", "Lowers Party Physical damage taken by 3%.", "Physical party dmg taken -3%"), 
				new SummonPassive("Physical Power Up", "Raises Physical BRV damage dealt by 10%.", "Physical dmg dealt +10%", true),
				new SummonPassive("Odin Attack Up", "Raises Attack by 5% when HP > 60%.", "ATK+5% if HP>60%", true), 
				new SummonPassive("Odin Base&Attack Up", "Raises Initial BRV by 5% and ATK by 2% when HP > 60%.", "IBrv+5% & ATK+2% if HP>60%", true), 
				new SummonPassive("Odin Sneak Power", "Raises BRV damage dealt by 10% when not being targetted", "BRV dmg dealt +10% if not targetted", true), 
				new SummonPassive("Odin Receive BRV Heal Up", "Raises BRV gained by 5%", "BRV gained +5%", true));
	}
}