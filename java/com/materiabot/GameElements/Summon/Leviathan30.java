package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Leviathan30 extends _Summon{
	protected Leviathan30() {
		super(Arrays.asList("Leviathan", "Levi"), 30, Element.Water, "Tidal Wave", 
				"Lowers enemy Speed by 15%" + System.lineSeparator() + "Grants 80% Initial BRV Regen after hitting HP < 80% once", 
					"Water BRV Magic damage" + System.lineSeparator() + 
					"Grants Water attribute to the party", 
				"6|14|12|8|10", "Krile|Laguna|Zell|Kefka|Tifa|Shantotto", 6, 6000, "Moderately Fast", 
				new SummonPassive("Water Resist Up", "Lowers Water damage taken by 10%.", "Water dmg taken -10%"),
				new SummonPassive("Water Resist All", "Lowers Party Water damage taken by 3%.", "Water party dmg taken -3%"), 
				new SummonPassive("Water Power Up", "Raises Water BRV damage dealt by 10%.", "Water dmg dealt +10%"),
				new SummonPassive("Leviathan Boost Up", "Raises Max BRV by 10% when HP < 80%.", "MBrv+10% if HP<80%", true), 
				new SummonPassive("Leviathan Base&Attack Up", "Raises Initial BRV by 10% and Attack by 2% when HP < 80%.", "IBrv+10% & ATK+2% if HP<80%", true), 
				new SummonPassive("Leviathan Life Up", "Raises Max HP by 10%.", "HP+10%", true), 
				new SummonPassive("Leviathan Resist Up", "Lowers BRV damage taken by 5% when HP < 80%.", "BRV dmg taken -5% if HP<80%"));
	}
}