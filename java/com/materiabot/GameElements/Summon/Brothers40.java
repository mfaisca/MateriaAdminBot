package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Brothers40 extends _Summon{
	protected Brothers40() {
		super(Arrays.asList("Brothers", "Brother", "Sacred", "Minotaur"), 40, Element.Earth, "Brotherly Love+", 
				"Raises Max BRV by 30% when HP > 50%", 
					"Earth BRV-HP Melee damage" + System.lineSeparator() + 
					"Grants Earth attribute to the party" + System.lineSeparator() + 
					"Party +10% Maximum BRV damage limit" + System.lineSeparator() + 
					"Delays all enemies by 1 turn",  
				"12|10|14|6|8", "Yuffie|Bartz|Galuf|Zidane|Paine|Shadow", 6, 6000, "Moderately Fast", 
				new SummonPassive("Earth Resist Up", "Lowers Earth damage taken by 10%.", "Earth dmg taken -10%"),
				new SummonPassive("Earth Resist All", "Lowers Party Earth damage taken by 3%.", "Earth party dmg taken -3%"), 
				new SummonPassive("Earth Power Up", "Raises Earth BRV damage dealt by 10%.", "Earth dmg dealt +10%"),
				new SummonPassive("Brothers Boost Up", "Raises Max BRV by 5% when HP > 60% Max HP", "MBrv+5% if HP>60%", true), 
				new SummonPassive("Brothers Boost Guard Up", "Raises Max BRV by 2% and Defense by 10% when HP > 60% Max HP", "MBrv+2% & DEF+10% if HP>60%", true), 
				new SummonPassive("Brothers Receive Heal Up", "Raises HP recovered by 5% when BRV > 50% Max BRV", "HP heal received +5% if BRV>50%"), 
				new SummonPassive("Brothers Protect Up", "Lowers HP Damage taken by 5% when BRV > 50% Max BRV", "HP dmg taken -5% if BRV>50%", true));
	}
}