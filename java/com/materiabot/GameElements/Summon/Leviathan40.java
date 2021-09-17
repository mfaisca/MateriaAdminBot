package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public class Leviathan40 extends _Summon{
	protected Leviathan40() {
		super(Arrays.asList("Leviathan", "Levi"), 40, Element.Water, "Tidal Wave+", 
				"Lowers enemy Speed by 15%" + System.lineSeparator() + "Grants 80% Initial BRV Regen after hitting HP < 80% once", 
					"Water BRV-HP Magic damage" + System.lineSeparator() + 
					"Grants Water attribute to the party" + System.lineSeparator() + 
					"Party +10% Maximum BRV damage limit" + System.lineSeparator() + 
					"Grants Party " + ImageUtils.getEmoteText("ailmentSilver") + ImageUtils.getAilmentEmote(17, -1, false, -1, -1) + Methods.enframe("Tsunami") + " for 2 turns:" + System.lineSeparator() + 
					"\t+20% Maximum BRV damage limit" + System.lineSeparator() + 
					"\tRaises BRV by 50% of HP Damage Dealt", 
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