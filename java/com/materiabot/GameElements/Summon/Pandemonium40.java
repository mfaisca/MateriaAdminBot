package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Pandemonium40 extends _Summon{
	protected Pandemonium40() {
		super(Arrays.asList("Pandemonium", "Pandemona", "Panda"), 40, Element.Wind, "Tornado Zone+", 
				"Raises BRV and HP damage by 25% during a Chase Sequence", 
					"Wind BRV-HP Magic damage" + System.lineSeparator() + 
					"Grants Wind attribute to the party" + System.lineSeparator() + 
					"Party +10% Maximum BRV damage limit" + System.lineSeparator() + 
					"Initiates a chase sequence (50 [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))",   
				"10|14|6|8|12", "Edge|Penelo|Y'shtola|Vivi|Freya|Lion", 6, 6000, "Moderately Fast", 
				new SummonPassive("Wind Resist Up", "Lowers Wind damage taken by 10%.", "Wind dmg taken -10%"),
				new SummonPassive("Wind Resist All", "Lowers Party Wind damage taken by 3%.", "Wind party dmg taken -3%"), 
				new SummonPassive("Wind Power Up", "Raises Wind BRV damage dealt by 10%.", "Wind dmg dealt +10%"),
				new SummonPassive("Pandemonium Attack Up", "Raises Attack by 10% when HP < 80% Max HP", "ATK+10% if HP<80%", true), 
				new SummonPassive("Pandemonium Boost Guard Up", "Raises Max BRV by 2% and Defense by 20% when HP < 80% Max HP", "MBrv+2% & DEF+20% if HP<80%", true), 
				new SummonPassive("Pandemonium Chase HP Damage Up", "Raises HP damage dealt during a Chase Sequence by 5%", "Chase HP dmg dealt +5%", true), 
				new SummonPassive("Pandemonium Chase BRV Damage Up", "Raises BRV damage dealt during a Chase Sequence by 20% when HP < 80%.", "Chase BRV dmg dealt +20% if HP<80%"));
	}
}