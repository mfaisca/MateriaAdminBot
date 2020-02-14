package com.materiabot;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.factory.PluginSources;

import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements._Library;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;

public class PluginManager {
	public static void loadCommands() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		manager.loadPlugins(PluginSources.jarSource("file:/" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));

		//manager.getAllPlugins().stream().forEach(p -> System.out.println(p.getName()));
//		for(PluginRef plugin : manager.getAllPlugins()) {
//			System.out.println(plugin.getName());
//		}
		
		//LoadCommands
		_Listener.unloadPluginCommands();
		manager.getAllPlugins().stream()
			.filter(p -> p.getName().contains("Command."))
			.sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
			.map(p -> (_BaseCommand)p.get())
			.forEach(c -> _Listener.COMMANDS.add(c));
	}
	
	public static void loadUnits() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		manager.loadPlugins(PluginSources.jarSource("file:/" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Library.GL.UNIT_LIST.clear();
		_Library.JP.UNIT_LIST.clear();
		manager.getAllPlugins().stream()
			.filter(p -> p.getName().contains("Unit."))
			.sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
			.map(p -> (Unit)p.get())
			.peek(c -> _Library.GL.UNIT_LIST.add(c))
			.forEach(c -> _Library.JP.UNIT_LIST.add(c));
	}
}