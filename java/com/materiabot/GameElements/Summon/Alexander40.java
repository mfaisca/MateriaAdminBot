package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Alexander40 extends _Summon{
	protected Alexander40() {
		super(Arrays.asList("Alexander", "Alex"), 40, Element.Holy, "Divine Judgment+", 
				"Raises Max HP by 15%" + System.lineSeparator() + "Grants 15% HP Regen for 10 turns after hitting HP < 80% once", 
					"Holy BRV Magic damage" + System.lineSeparator() + 
					"Grants Holy attribute to the party" + System.lineSeparator() + 
					"Party +10% Maximum BRV damage limit" + System.lineSeparator() + 
					"Recover party HP by 100% of Max HP",
				"14|8|12|10|6", "Sazh|Raijin|Rosa|Tidus|Ashe|Firion", 6, 6000, "Moderately Fast", 
				new SummonPassive("Holy Resist Up", "Lowers Holy damage taken by 10%.", "Holy dmg taken -10%"),
				new SummonPassive("Holy Resist All", "Lowers Party Holy damage taken by 3%.", "Holy party dmg taken -3%"), 
				new SummonPassive("Holy Power Up", "Raises Holy BRV damage dealt by 10%.", "Holy dmg dealt +10%"),
				new SummonPassive("Alexander Boost Up", "Raises Max BRV by 5% after hitting HP<60% once", "MBrv+5% if HP<60% once", true), 
				new SummonPassive("Alexander Base Attack Up", "Raises Initial BRV by 5% and Attack by 2% after hitting HP<60% once", "IBrv+5% & ATK+2% if HP<60% once", true), 
				new SummonPassive("Alexander Target Power", "When attacking an enemy targeting self:\n- Increases BRV damage dealt by 10%", "BRV dmg dealt +10% if enemy targetting self", true), 
				new SummonPassive("Alexander Receive Heal Up", "When BRV < 50% Max BRV- Increases HP recovered by 5%", "HP recovered +5% if MaxBRV<50%"));
	}
}