package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Sylph20 extends _Summon{
	protected Sylph20() {
		super(Arrays.asList("Sylph"), 20, Element.Wind, "Whispering Wind", 
				"Raises Debuff Evasion Rate by 30% when BRV >= Initial BRV", 
					"BRV and HP recovery" + System.lineSeparator() + 
					"Grants Wind attribute to the party", 
				null, null, 5, 2000, "Fast");
	}
}