package com.materiabot;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.plugface.core.PluginRef;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.factory.PluginSources;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators._Plugin;

public class PluginManager {
	private PluginManager() {}

	public static void reset() throws Exception {
		_Library.L.UNIT_CACHE.invalidateAll();
		Constants.COMMANDS.clear();
		Constants.UNITS.clear();
		Constants.ABILITY_EFFECT.clear();
		Constants.AILMENT_EFFECT.clear();
		Constants.AILMENT_REQUIRED.clear();
		Constants.AURA_EFFECT.clear();
		Constants.AURA_REQUIRED.clear();
		Constants.LABELS.clear();
		PluginManager.loadEffects();
		PluginManager.loadUnits();
		PluginManager.loadCommands();
	}
	
	private static boolean effectsFilter(PluginRef<?> p) {
		return p.getName().contains("Passive.") || 
				p.getName().contains("Ability.") || 
				p.getName().contains("Ailment.") || 
				p.getName().contains("Aura.") || 
				p.getName().contains("Misc.");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void loadEffects() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(PluginManager::effectsFilter).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Listener.unloadPluginCommands();
		for(PluginRef p : manager.getAllPlugins().stream().filter(PluginManager::effectsFilter).collect(Collectors.toList())) {
			_Plugin eff = (_Plugin) p.get();
			HashMap map = null;
			switch(p.getName().substring(0, p.getName().lastIndexOf("."))) {
				case "Passive.Effect":
					map = Constants.PASSIVE_EFFECT; break;
				case "Passive.Required":
					map = Constants.PASSIVE_REQUIRED; break;
				case "Ability.Effect":
					map = Constants.ABILITY_EFFECT; break;
				case "Ailment.Effect":
					map = Constants.AILMENT_EFFECT; break;
				case "Aura.Effect":
					map = Constants.AURA_EFFECT; break;
				case "Aura.Required":
					map = Constants.AURA_REQUIRED; break;
				case "Misc.Condition":
					map = Constants.LABELS; break;
				default: ;
			}
			if(map != null)
				map.put(eff.getId(), eff);
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	public static void loadCommands() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Command.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Listener.unloadPluginCommands();
        Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).retrieveCommands().complete().stream()
    		.forEach(c -> Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).deleteCommandById(c.getId()));
//        Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).retrieveCommands().complete().stream()
//    		.forEach(c -> Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).deleteCommandById(c.getId()));
		manager.getAllPlugins().stream()
			.filter(p -> p.getName().contains("Command."))
			.map(p -> (_BaseCommand)p.get())
			.forEach(c -> Constants.COMMANDS.add(c));
		CommandListUpdateAction commands = Constants.getClient().updateCommands();
		for(_BaseCommand c : Constants.COMMANDS) {
			if(c.getCommandData() == null)
				;
//			else if(Constants.DEBUG)
//				Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).upsertCommand(c.getCommandData()).queue();
			else
				commands.addCommands(c.getCommandData());
			if(c.getAdminCommandData() != null)
				Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).upsertCommand(c.getAdminCommandData()).queue();
		}
        commands.queue();
	}

	@SuppressWarnings("rawtypes")
	public static void loadUnits() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Unit.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadUnits
		Constants.UNITS.clear();
		Constants.UNITS.addAll(manager.getAllPlugins().stream()
							.filter(p -> p.getName().contains("Unit."))
							.map(p -> (Unit)p.get())
							.map(p -> {
								System.out.print("Reading " + p.getName() + "...");
								Unit u = _Library.L.getQuickUnit(p.getName());
								p.setCrystal(u.getCrystal());
								p.setEquipmentType(u.getEquipmentType());
								p.setSphereSlots(u.getSphereSlots());;
								System.out.println(" OK");
								return u;
							})
							.collect(Collectors.toList()));
	}
}