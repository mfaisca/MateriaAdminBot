package com.materiabot;
import java.util.stream.Collectors;
import org.plugface.core.PluginRef;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.factory.PluginSources;
import com.materiabot.IO.JSON.UnitParser;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Effect._AbilityEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect._AuraEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Required._AuraRequired;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;

public class PluginManager {
	@SuppressWarnings("rawtypes")
	public static void loadEffects() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Passive.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Listener.unloadPluginCommands();
		manager.getAllPlugins().stream()
			.filter(p -> p.getName().contains("Passive.Effect"))
			.map(p -> (com.materiabot.GameElements.Enumerators.Passive.Effect._PassiveEffect)p.get())
			.forEach(c -> Constants.PASSIVE_EFFECT.put(c.getId(), c));
		manager.getAllPlugins().stream()
		.filter(p -> p.getName().contains("Passive.Required"))
		.map(p -> (com.materiabot.GameElements.Enumerators.Passive.Required._PassiveRequired)p.get())
		.forEach(c -> Constants.PASSIVE_REQUIRED.put(c.getId(), c));
		manager.getAllPlugins().stream()
		.filter(p -> p.getName().contains("Ability.Effect"))
		.map(p -> (_AbilityEffect)p.get())
		.forEach(c -> Constants.ABILITY_EFFECT.put(c.getId(), c));
		manager.getAllPlugins().stream()
		.filter(p -> p.getName().contains("Ailment.Effect"))
		.map(p -> (_AilmentEffect)p.get())
		.forEach(c -> Constants.AILMENT_EFFECT.put(c.getId(), c));
//		manager.getAllPlugins().stream()
//		.filter(p -> p.getName().contains("Ailment.Required"))
//		.map(p -> (_AilmentRequired)p.get())
//		.forEach(c -> Constants.AILMENT_REQUIRED.put(""+c.getId(), c));
//		manager.getAllPlugins().stream()
//		.filter(p -> p.getName().contains("Ailment.Condition"))
//		.map(p -> (_Condition)p.get())
//		.forEach(c -> Constants.AILMENT_CONDITION.put(c.getId(), c));
		manager.getAllPlugins().stream()
		.filter(p -> p.getName().contains("Aura.Effect"))
		.map(p -> (_AuraEffect)p.get())
		.forEach(c -> Constants.AURA_EFFECT.put(c.getId(), c));
		manager.getAllPlugins().stream()
		.filter(p -> p.getName().contains("Aura.Required"))
		.map(p -> (_AuraRequired)p.get())
		.forEach(c -> Constants.AURA_REQUIRED.put(c.getId(), c));
	}
	
	@SuppressWarnings("rawtypes")
	public static void loadCommands() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Command.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Listener.unloadPluginCommands();
		manager.getAllPlugins().stream()
			.filter(p -> p.getName().contains("Command."))
			.map(p -> (_BaseCommand)p.get())
			.forEach(c -> Constants.COMMANDS.add(c));
		Constants.COMMANDS.sort((c1, c2) -> c1.getCommand().compareTo(c2.getCommand()));
	}

	@SuppressWarnings("rawtypes")
	public static void loadUnits() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Unit.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadUnits
		UnitParser.UNITS.clear();
		UnitParser.UNITS = manager.getAllPlugins().stream()
							.filter(p -> p.getName().contains("Unit."))
							.map(p -> (Unit)p.get())
							.map(p -> { //TODO DEBUG
								System.out.print("Reading " + p.getName() + "...");
								Unit u = _Library.L.getQuickUnit(p.getName());
								//Unit u = _Library.L.getUnit(p.getName()); //TODO DEBUG
								p.setCrystal(u.getCrystal());
								p.setEquipmentType(u.getEquipmentType());
								p.setSphereSlots(u.getSphereSlots());;
								System.out.println(" OK");
								return u;
							})
							.collect(Collectors.toList());
	}
}