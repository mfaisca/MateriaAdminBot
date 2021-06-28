package com.materiabot;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		PluginManager.loadCommands();
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		
		_Library.L.getUnit("Squall").getAbility(AttackName.S1).stream().forEach(p -> {
			String text = p.getName().getBest() + " (ID: " + p.getId() + ")" + System.lineSeparator() + System.lineSeparator() + p.generateDescription();
			System.out.println(text);
		});
	}
}