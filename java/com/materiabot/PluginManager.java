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

public class PluginManager {
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
							.peek(p -> {
								System.out.print("Reading " + p.getName() + "...");
								Unit u = _Library.L.getQuickUnit(p.getName());
								p.setCrystal(u.getCrystal());
								p.setEquipmentType(u.getEquipmentType());
								p.setSphereSlots(u.getSphereSlots());
								System.out.println(" OK");
							}).collect(Collectors.toList());
	}
}