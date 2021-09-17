package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Diabolos30 extends _Summon{
	protected Diabolos30() {
		super(Arrays.asList("Diabolos", "Diablos", "Diablo"), 30, Element.Dark, "Dark Messenger", 
				"Raises Initial BRV by 50%" + System.lineSeparator()
				+ "+50% BRV Damage when hitting elemental weakness after hitting HP < 80% once", 
					"Dark BRV Magic damage" + System.lineSeparator() + 
					"Grants Dark attribute to the party",
				"12|10|14|6|8", "Wakka|Aerith|Deuce|Kuja|Sabin|Vaan", 6, 6000, "Moderately Fast", 
				new SummonPassive("Dark Resist Up", "Lowers Dark damage taken by 10%.", "Dark dmg taken -10%"),
				new SummonPassive("Dark Resist All", "Lowers Party Dark damage taken by 3%.", "Dark party dmg taken -3%"), 
				new SummonPassive("Dark Power Up", "Raises Dark BRV damage dealt by 10%.", "Dark dmg dealt +10%"),
				new SummonPassive("Diabolos Attack Up", "Raises Attack by 5% after hitting HP<60% once", "ATK+5% if HP<60% once", true), 
				new SummonPassive("Diabolos Boost Guard Up", "Raises Max BRV by 2% and Defense by 10% when HP < 60% once", "MBrv+2% & DEF+20% if HP<80% once", true), 
				new SummonPassive("Diabolos Receive BRV Heal Up", "Raises BRV gained by 5%", "BRV Gained +5%", true), 
				new SummonPassive("Diabolos Protect Up", "Lowers HP Damage taken by 5% when Max BRV < 50%", "HP dmg taken -5% if BRV<50%"));
	}
}