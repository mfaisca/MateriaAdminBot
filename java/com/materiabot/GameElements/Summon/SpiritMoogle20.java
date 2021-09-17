package com.materiabot.GameElements.Summon;
import java.util.Arrays;
import com.materiabot.GameElements.Element;

public class SpiritMoogle20 extends _Summon{
	protected SpiritMoogle20() {
		super(Arrays.asList("Spirit Moogle", "Spirit Mog", "Moogle", "Mog"), 20, Element.Null, "Don't fluff me, kupo~", 
				"Increases Terra's Affection by 10", 
					"Removes Terra's debuffs" + System.lineSeparator() + 
					"Raises Terra's ATK by 10%", 
				null, null, 5, 2000, "Fast");
	}
}