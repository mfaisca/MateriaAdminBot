package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Shiva30 extends _Summon{
	protected Shiva30() {
		super(Arrays.asList("Shiva"), 30, Element.Ice, "Diamond Dust", 
				"Raises Attack by 15%" + System.lineSeparator() + "Raises Speed by 20%" + System.lineSeparator() + "Raises EX Recast by 5%", 
					"Ice BRV Magic damage" + System.lineSeparator() + 
					"Grants Ice attribute to the party", 
				"14|12|8|10|6", "Serah|Seifer|Thancred|Steiner|Auron|Prishe", 6, 6000, "Moderately Fast", 
				new SummonPassive("Ice Resist Up", "Lowers Ice damage taken by 10%.", "Ice dmg taken -10%"),
				new SummonPassive("Ice Resist All", "Lowers Party Ice damage taken by 3%.", "Ice party dmg taken -3%"), 
				new SummonPassive("Ice Power Up", "Raises Ice BRV damage dealt by 10%.", "Ice dmg dealt +10%"),
				new SummonPassive("Shiva Evasion Up", "Raises Evasion Rate by 10% when HP < 80%.", "Evade+10% if HP<80%", true), 
				new SummonPassive("Shiva Base&Guard Up", "Raises Initial BRV by 10% and Defense by 20% when HP < 80%.", "IBrv+10% & DEF+20% if HP<80%", true), 
				new SummonPassive("Shiva Bonus Up", "Raises Break Bonus by 5% when HP < 80%.", "Break Bonus +5% if HP<80%"), 
				new SummonPassive("Shiva Critical Power Up", "Raises Critical BRV damage by 10% when HP < 80%.", "Crit dmg dealt +10% if HP<80%", true));
	}
}