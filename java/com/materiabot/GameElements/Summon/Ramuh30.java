package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Ramuh30 extends _Summon{
	protected Ramuh30() {
		super(Arrays.asList("Ramuh"), 30, Element.Thunder, "Judgement Bolt", 
				"Raises Max BRV by 15%" + System.lineSeparator() + "Raises Defense by 80% after hitting HP < 80% once", 
					"Lightning BRV Magic damage" + System.lineSeparator() + 
					"Grants Lightning attribute to the party", 
				"8|6|10|12|14", "Setzer|Onion Knight|Lenna|Maria|Cid|Eiko", 6, 6000, "Moderately Fast", 
				new SummonPassive("Lightning Resist Up", "Lowers Lightning damage taken by 10%.", "Lightning dmg taken -10%"),
				new SummonPassive("Lightning Resist All", "Lowers Party Lightning damage taken by 3%.", "Lightning party dmg taken -3%"), 
				new SummonPassive("Lightning Power Up", "Raises Lightning BRV damage dealt by 10%.", "Lightning dmg dealt +10%"),
				new SummonPassive("Ramuh Evasion Up", "Raises Evasion Rate by 10% when HP > 80%.", "Evade+10% if HP>80%", true), 
				new SummonPassive("Ramuh Base&Guard Up", "Raises Initial BRV by 10% and Defense by 20% when HP > 80%.", "IBrv+10% & DEF+20% if HP>80%", true), 
				new SummonPassive("Ramuh Chase BRV Damage Up", "Raises BRV damage dealt during a Chase Sequence by 10% when HP > 80%.", "Chase BRV dmg dealt +10% if HP>80%"), 
				new SummonPassive("Ramuh Resist Up", "Lowers BRV damage taken by 5% when HP > 80%.", "BRV dmg taken -5% if HP>80%", true));
	}
}