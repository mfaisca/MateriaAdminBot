package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class Chocobo20 extends _Summon{
	protected Chocobo20() {
		super(Arrays.asList("Chocobo", "Choco"), 20, Element.Null, "Chocobo Kick", 
				"Raises party's HP by 250", 
					"Non-elemental BRV Physical damage" + System.lineSeparator() + 
					"Raises Attack by 10%", 
				null, null, 5, 2000, "Fast");
	}
}