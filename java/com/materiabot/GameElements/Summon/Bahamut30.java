package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Bahamut30 extends _Summon{
	protected Bahamut30() {
		super(Arrays.asList("Bahamut", "Baha"), 30, Element.Null, "Megaflare", 
				"All enemies 15% Attack Down" + System.lineSeparator() + "Raises BREAK Bonus by 50% after an enemy is broken (up to 200%)", 
					"Non-elemental BRV Magic damage" + System.lineSeparator() + 
					"Raises non-elemental BRV damage by 100%", 
				"6|12|8|14|10", "Kuja|Rydia|Garnet|Lulu|Papalymo|Porom", 6, 6000, "Moderately Fast", 
				new SummonPassive("Magic Resist Up", "Lowers Magic damage taken by 10%.", "Magic dmg taken -10%"),
				new SummonPassive("Magic Resist All", "Lowers Party Magic damage taken by 3%.", "Magic party dmg taken -3%"), 
				new SummonPassive("Magic Power Up", "Raises Magic BRV damage dealt by 10%.", "Magic dmg dealt +10%", true),
				new SummonPassive("Bahamut Boost Up", "Raises Max BRV by 10% when HP > 80% Max HP", "MBrv+10% if HP>80%", true), 
				new SummonPassive("Bahamut Boost Guard Up", "Raises Max BRV by 2% and Defense by 20% when HP > 80% Max HP", "MBrv+2% & DEF+20% if HP>80%", true), 
				new SummonPassive("Bahamut Life Up", "Raises Max HP by 10%.", "HP+10%", true), 
				new SummonPassive("Bahamut Knock Back HP Damage Up", "Raises HP damage dealt during a Chase Sequence by 5%", "Chase HP dmg dealt +5%"));
	}
}